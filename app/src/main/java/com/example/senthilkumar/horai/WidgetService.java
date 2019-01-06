package com.example.senthilkumar.horai;

import android.content.Intent;
import android.widget.RemoteViewsService;
import android.widget.Toast;

/**
 * WidgetService is the {@link RemoteViewsService} that will return our RemoteViewsFactory
 */
public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Toast.makeText(this, "Widget Refreshed from service", Toast.LENGTH_LONG).show();
        return new WidgetDataProvider(this, intent);
    }
}
