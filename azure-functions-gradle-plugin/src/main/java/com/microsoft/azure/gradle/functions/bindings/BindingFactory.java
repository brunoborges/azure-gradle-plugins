package com.microsoft.azure.gradle.functions.bindings;

import com.microsoft.azure.functions.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BindingFactory {
    private static Map<Class<? extends Annotation>, Class<? extends BaseBinding>> map = new ConcurrentHashMap();

    static {
        map.put(BlobTrigger.class, BlobBinding.class);
        map.put(BlobInput.class, BlobBinding.class);
        map.put(BlobOutput.class, BlobBinding.class);
        map.put(CosmosDBInput.class, CosmosDBBinding.class);
        map.put(CosmosDBOutput.class, CosmosDBBinding.class);
        map.put(CosmosDBTrigger.class, CosmosDBBinding.class);
        map.put(EventHubTrigger.class, EventHubBinding.class);
        map.put(EventHubOutput.class, EventHubBinding.class);
        map.put(EventGridTrigger.class, EventGridBinding.class);
        map.put(HttpTrigger.class, HttpBinding.class);
        map.put(HttpOutput.class, HttpBinding.class);
        map.put(MobileTableInput.class, MobileTableBinding.class);
        map.put(MobileTableOutput.class, MobileTableBinding.class);
        map.put(NotificationHubOutput.class, NotificationHubBinding.class);
        map.put(QueueTrigger.class, QueueBinding.class);
        map.put(QueueOutput.class, QueueBinding.class);
        map.put(SendGridOutput.class, SendGridBinding.class);
        map.put(ServiceBusQueueTrigger.class, ServiceBusBinding.class);
        map.put(ServiceBusTopicTrigger.class, ServiceBusBinding.class);
        map.put(ServiceBusQueueOutput.class, ServiceBusBinding.class);
        map.put(ServiceBusTopicOutput.class, ServiceBusBinding.class);
        map.put(TableInput.class, TableBinding.class);
        map.put(TableOutput.class, TableBinding.class);
        map.put(TimerTrigger.class, TimerBinding.class);
        map.put(TwilioSmsOutput.class, TwilioBinding.class);
    }

    public static BaseBinding getBinding(final Annotation annotation) {
        final Class<? extends Annotation> annotationType = annotation.annotationType();
        return map.containsKey(annotationType) ?
                createNewInstance(map.get(annotationType), annotation) :
                null;
    }

    private static BaseBinding createNewInstance(final Class<? extends BaseBinding> binding,
                                                 final Annotation annotation) {
        try {
            final Class<? extends Annotation> annotationType = annotation.annotationType();
            final Constructor constructor = binding.getConstructor(annotationType);
            return (BaseBinding) constructor.newInstance(annotation);
        } catch (Exception e) {
            // Swallow it
        }
        return null;
    }
}
