package rocks.inspectit.agent.java.sensor.method.remote.client;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.testng.annotations.Test;

import rocks.inspectit.agent.java.config.impl.RegisteredSensorConfig;
import rocks.inspectit.agent.java.core.ICoreService;
import rocks.inspectit.agent.java.core.IPlatformManager;
import rocks.inspectit.agent.java.sdk.opentracing.internal.impl.SpanContextImpl;
import rocks.inspectit.agent.java.sdk.opentracing.internal.impl.SpanImpl;
import rocks.inspectit.agent.java.tracing.core.ClientInterceptor;
import rocks.inspectit.agent.java.tracing.core.adapter.ClientAdapterProvider;
import rocks.inspectit.agent.java.tracing.core.adapter.ClientRequestAdapter;
import rocks.inspectit.agent.java.tracing.core.adapter.ResponseAdapter;
import rocks.inspectit.shared.all.testbase.TestBase;
import rocks.inspectit.shared.all.tracing.data.ClientSpan;

/**
 * @author Ivan Senic
 *
 */
@SuppressWarnings("PMD")
public class RemoteClientHookTest extends TestBase {

	@InjectMocks
	RemoteClientHook hook;

	@Mock
	ICoreService coreService;

	@Mock
	ClientInterceptor clientInterceptor;

	@Mock
	ClientAdapterProvider adapterProvider;

	@Mock
	ClientRequestAdapter<?> requestAdapter;

	@Mock
	ResponseAdapter responseAdapter;

	@Mock
	IPlatformManager platformManager;

	@Mock
	Object object;

	@Mock
	Object result;

	@Mock
	RegisteredSensorConfig rsc;

	@Test
	public void happyPath() throws Exception {
		// ids
		long platformId = 1l;
		long methodId = 7l;
		long sensorId = 13l;
		long spanId = 17l;
		// platform
		when(platformManager.getPlatformId()).thenReturn(platformId);
		// interceptor
		Object[] parameters = new String[] { "blah", "bla" };
		doReturn(requestAdapter).when(adapterProvider).getClientRequestAdapter(object, parameters, rsc);
		doReturn(responseAdapter).when(adapterProvider).getClientResponseAdapter(object, parameters, result, rsc);

		SpanContextImpl context = mock(SpanContextImpl.class);
		when(context.getId()).thenReturn(spanId);
		SpanImpl spanImpl = mock(SpanImpl.class);
		when(spanImpl.context()).thenReturn(context);
		when(spanImpl.isClient()).thenReturn(true);
		when(clientInterceptor.handleRequest(requestAdapter)).thenReturn(spanImpl);
		when(clientInterceptor.handleResponse(spanImpl, responseAdapter)).thenReturn(spanImpl);

		// execute calls
		hook.beforeBody(methodId, sensorId, object, parameters, rsc);
		hook.firstAfterBody(methodId, sensorId, object, parameters, result, rsc);
		hook.secondAfterBody(coreService, methodId, sensorId, object, parameters, result, rsc);

		ArgumentCaptor<ClientSpan> captor = ArgumentCaptor.forClass(ClientSpan.class);
		verify(coreService).addMethodSensorData(eq(sensorId), eq(methodId), eq(String.valueOf(spanId)), captor.capture());
		assertThat(captor.getValue().getPlatformIdent(), is(platformId));
		assertThat(captor.getValue().getSensorTypeIdent(), is(sensorId));
		assertThat(captor.getValue().getMethodIdent(), is(methodId));

		// verify timer, interceptor and adapters
		verify(clientInterceptor).handleRequest(requestAdapter);
		verify(clientInterceptor).handleResponse(spanImpl, responseAdapter);
		verify(adapterProvider).getClientRequestAdapter(object, parameters, rsc);
		verify(adapterProvider).getClientResponseAdapter(object, parameters, result, rsc);
		verifyNoMoreInteractions(adapterProvider, clientInterceptor, coreService);
	}

	@Test
	public void spanNull() throws Exception {
		// ids
		long platformId = 1l;
		long methodId = 7l;
		long sensorId = 13l;
		// platform
		when(platformManager.getPlatformId()).thenReturn(platformId);
		// interceptor
		Object[] parameters = new String[] { "blah", "bla" };
		doReturn(requestAdapter).when(adapterProvider).getClientRequestAdapter(object, parameters, rsc);
		doReturn(responseAdapter).when(adapterProvider).getClientResponseAdapter(object, parameters, result, rsc);
		when(clientInterceptor.handleRequest(requestAdapter)).thenReturn(null);

		// execute calls
		hook.beforeBody(methodId, sensorId, object, parameters, rsc);
		hook.firstAfterBody(methodId, sensorId, object, parameters, result, rsc);
		hook.secondAfterBody(coreService, methodId, sensorId, object, parameters, result, rsc);

		// verify timer, interceptor and adapters
		verify(clientInterceptor).handleRequest(requestAdapter);
		verify(adapterProvider).getClientRequestAdapter(object, parameters, rsc);
		verifyNoMoreInteractions(adapterProvider, clientInterceptor, coreService);
		verifyZeroInteractions(coreService);
	}

	@Test
	public void twoCalls() throws Exception {
		// ids
		long platformId = 1l;
		long methodId = 7l;
		long sensorId = 13l;
		long spanId = 17l;
		// platform
		when(platformManager.getPlatformId()).thenReturn(platformId);
		// interceptor
		Object[] parameters = new String[] { "blah", "bla" };
		doReturn(requestAdapter).when(adapterProvider).getClientRequestAdapter(object, parameters, rsc);
		doReturn(responseAdapter).when(adapterProvider).getClientResponseAdapter(object, parameters, result, rsc);
		SpanContextImpl context = mock(SpanContextImpl.class);
		when(context.getId()).thenReturn(spanId);
		SpanImpl spanImpl = mock(SpanImpl.class);
		when(spanImpl.context()).thenReturn(context);
		when(spanImpl.isClient()).thenReturn(true);
		when(clientInterceptor.handleRequest(requestAdapter)).thenReturn(spanImpl);
		when(clientInterceptor.handleResponse(spanImpl, responseAdapter)).thenReturn(spanImpl);

		// execute calls
		hook.beforeBody(methodId, sensorId, object, parameters, rsc);
		// new call
		hook.beforeBody(methodId, sensorId, object, parameters, rsc);
		hook.firstAfterBody(methodId, sensorId, object, parameters, result, rsc);
		hook.secondAfterBody(coreService, methodId, sensorId, object, parameters, result, rsc);
		// end new call
		hook.firstAfterBody(methodId, sensorId, object, parameters, result, rsc);
		hook.secondAfterBody(coreService, methodId, sensorId, object, parameters, result, rsc);

		ArgumentCaptor<ClientSpan> captor = ArgumentCaptor.forClass(ClientSpan.class);
		verify(coreService).addMethodSensorData(eq(sensorId), eq(methodId), eq(String.valueOf(spanId)), captor.capture());
		assertThat(captor.getValue().getPlatformIdent(), is(platformId));
		assertThat(captor.getValue().getSensorTypeIdent(), is(sensorId));
		assertThat(captor.getValue().getMethodIdent(), is(methodId));

		// verify timer, interceptor and adapters
		verify(clientInterceptor).handleRequest(requestAdapter);
		verify(clientInterceptor).handleResponse(spanImpl, responseAdapter);
		verify(adapterProvider).getClientRequestAdapter(object, parameters, rsc);
		verify(adapterProvider).getClientResponseAdapter(object, parameters, result, rsc);
		verifyNoMoreInteractions(adapterProvider, clientInterceptor, coreService);
	}

}
