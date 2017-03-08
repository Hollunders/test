package rocks.inspectit.server.service.rest;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import io.gsonfire.GsonFireBuilder;
import io.gsonfire.TypeSelector;
import rocks.inspectit.agent.java.sdk.opentracing.internal.impl.SpanBuilderImpl;
import rocks.inspectit.agent.java.sdk.opentracing.internal.impl.SpanImpl;
import rocks.inspectit.agent.java.sdk.opentracing.internal.impl.TracerImpl;
import rocks.inspectit.agent.java.tracing.core.transformer.SpanTransformer;
import rocks.inspectit.server.cache.IBuffer;
import rocks.inspectit.server.cache.impl.BufferElement;
import rocks.inspectit.server.service.rest.error.JsonError;
import rocks.inspectit.server.util.CacheIdGenerator;
import rocks.inspectit.server.util.PlatformIdentCache;
import rocks.inspectit.shared.all.communication.DefaultData;
import rocks.inspectit.shared.all.communication.data.InvocationSequenceData;
import rocks.inspectit.shared.all.communication.data.MobilePeriodicMeasurement;
import rocks.inspectit.shared.all.tracing.data.AbstractSpan;
import rocks.inspectit.shared.all.tracing.data.SpanIdent;

/**
 * Restful service provider for detail information.
 *
 * @author Tobias Angerstein, Alper Hidiroglu, Manuel Palenga
 *
 */
@Controller
@RequestMapping(value = "/mobile/newinvocation")
public class AgentRestfulService {

	@Autowired
	private PlatformIdentCache platformCache;

	@Autowired
	private IBuffer<DefaultData> buffer;

	@Autowired
	private CacheIdGenerator idGenerator;

	/**
	 * Handling of all the exceptions happening in this controller.
	 *
	 * @param exception
	 *            Exception being thrown
	 * @return {@link ModelAndView}
	 */
	@ExceptionHandler(Exception.class)
	public ModelAndView handleAllException(Exception exception) {
		return new JsonError(exception).asModelAndView();
	}

	@RequestMapping(method = POST, value = "")
	@ResponseBody
	public void addNewMobileRoot(@RequestBody String json) {

		Gson gson = getGson();
		MobileRoot mobileRoot = gson.fromJson(json, MobileRoot.class);

		for (SpanImpl span : mobileRoot.spans) {
			span.setTag("deviceID", mobileRoot.getDeviceID());
			AbstractSpan abstractSpan = SpanTransformer.transformSpan(span);
			abstractSpan.setPlatformIdent(-1);
			abstractSpan.setMethodIdent(0);
			abstractSpan.setSensorTypeIdent(0);
			idGenerator.assignObjectAnId(abstractSpan);

			buffer.put(new BufferElement<DefaultData>((DefaultData) abstractSpan));
		}

		for (MobilePeriodicMeasurement measurement : mobileRoot.measurements) {
			measurement.setDeviceID(mobileRoot.getDeviceID());
			measurement.setPlatformIdent(-2);
			measurement.setSensorTypeIdent(0);
			idGenerator.assignObjectAnId(measurement);

			measurement.setTimeStamp(new Timestamp(measurement.getTimestamp()));

			buffer.put(new BufferElement<DefaultData>((DefaultData) measurement));
		}
	}

	private Gson getGson() {
		GsonFireBuilder builder = new GsonFireBuilder().registerTypeSelector(DefaultData.class, new TypeSelector<DefaultData>() {

			@Override
			public Class<? extends DefaultData> getClassForElement(JsonElement readElement) {
				String type = readElement.getAsJsonObject().get("type").getAsString();

				if (type.equals("MobilePeriodicMeasurement")) {
					return MobilePeriodicMeasurement.class;
				} else {
					/*
					 * returning null will trigger Gson's default behavior
					 */
					return null;
				}
			}
		});
		Gson gson = builder.createGson();
		return gson;
	}

	/**
	 * Test method
	 */
	@RequestMapping(method = POST, value = "/defaultJSON")
	@ResponseBody
	public MobileRoot getNewMobileBeacon() {

		MobileRoot root = new MobileRoot();
		root.deviceID = 4242123456784242l;

		TracerImpl tracerImpl = new TracerImpl();
		SpanBuilderImpl usecaseBuilder = tracerImpl.buildSpan("Load screen");
		SpanImpl spanUsecase = usecaseBuilder.withTag("span.kind", "client").start();

		SpanBuilderImpl remoteBuilder = tracerImpl.buildSpan(null);
		SpanImpl spanRemoteCall = remoteBuilder.asChildOf(spanUsecase.context()).start();
		spanRemoteCall.setTag("span.kind", "server");

		// Global information
		spanRemoteCall.setTag("http.url", "localhost:8080/callRest");

		// Request information
		spanRemoteCall.setTag("http.request.ssid", "1234-5678");
		spanRemoteCall.setTag("http.request.networkConnection", "4G");
		spanRemoteCall.setTag("http.request.networkProvider", "MyProvider");
		spanRemoteCall.setTag("http.request.timeout", "false");
		spanRemoteCall.setTag("http.request.longitude", "48.421");
		spanRemoteCall.setTag("http.request.latitude", "13.12345");
		spanRemoteCall.setTag("http.request.responseCode", "200");

		// Response information
		spanRemoteCall.setTag("http.response.ssid", "1234-5678");
		spanRemoteCall.setTag("http.response.networkConnection", "4G");
		spanRemoteCall.setTag("http.response.networkProvider", "MyProvider");
		spanRemoteCall.setTag("http.response.timeout", "false");
		spanRemoteCall.setTag("http.response.longitude", "48.321");
		spanRemoteCall.setTag("http.response.latitude", "13.52345");

		List<MobilePeriodicMeasurement> measurements = new ArrayList<MobilePeriodicMeasurement>();
		measurements.add(new MobilePeriodicMeasurement(2423234524L, 12, 81.236218F, 83.24683246F, 12.12f));
		measurements.add(new MobilePeriodicMeasurement(2423234525L, 23, 96.99F, 10.500000F, 22.12f));
		measurements.add(new MobilePeriodicMeasurement(2423234726L, 34, 81.236218F, 70.24683246F, 12.12f));
		measurements.add(new MobilePeriodicMeasurement(2423234727L, 45, 80.99F, 11.500000F, 13.12f));
		measurements.add(new MobilePeriodicMeasurement(2423244828L, 56, 83.236218F, 82.24683246F, 12.12f));
		measurements.add(new MobilePeriodicMeasurement(2423244829L, 67, 97.99F, 11.400000F, 15.12f));

		root.spans.add(spanUsecase);
		root.spans.add(spanRemoteCall);
		root.measurements = measurements;

		return root;
	}

	/**
	 * Test method
	 */
	@RequestMapping(method = POST, value = "/createRemoteCall")
	@ResponseBody
	public void createRemoteCall() {
		InvocationSequenceData remoteCall = new InvocationSequenceData();
		remoteCall.setPlatformIdent(-1);
		remoteCall.setTimeStamp(new Timestamp(1000000000 + (new Random()).nextInt(1000)));
		remoteCall.setId(42);
		// RemoteCall ID which will be set by the header
		remoteCall.setSpanIdent(new SpanIdent(1, 0, 0));

		InvocationSequenceData remoteCall2 = new InvocationSequenceData();
		remoteCall2.setPlatformIdent(-1);
		remoteCall2.setTimeStamp(new Timestamp(1000000000 + (new Random()).nextInt(1000)));
		remoteCall2.setId(43);
		remoteCall2.setSpanIdent(new SpanIdent(2, 0, 0));
		// buffer.put(new BufferElement<InvocationSequenceData>(remoteCall));
		// buffer.put(new BufferElement<InvocationSequenceData>(remoteCall2));

	}

	/**
	 * Header information for swagger requests.
	 *
	 * @param response
	 *            Response information
	 */
	@ModelAttribute
	public void setVaryResponseHeader(HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
	}

	private class MobileRoot {

		private long deviceID;
		private List<SpanImpl> spans;
		private List<MobilePeriodicMeasurement> measurements;

		public MobileRoot() {
			spans = new ArrayList<SpanImpl>();
			measurements = new ArrayList<MobilePeriodicMeasurement>();
		}

		public void setDeviceID(long deviceID) {
			this.deviceID = deviceID;
		}

		public long getDeviceID() {
			return deviceID;
		}

		public List<MobilePeriodicMeasurement> getMeasurements() {
			return measurements;
		}

		public List<SpanImpl> getSpans() {
			return spans;
		}
	}
}
