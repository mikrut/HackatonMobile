package ru.bmstu.iu6.hackatonmobile.database;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.bmstu.iu6.hackatonmobile.R;

/**
 * Created by mikrut on 05.12.15.
 */
public class RequirementAdapter extends CursorAdapter {
    public RequirementAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.requirement_layout, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView tvHeader = (TextView) view.findViewById(R.id.header);
        TextView tvInfo = (TextView) view.findViewById(R.id.info);

        String[] typenames = context.getResources().getStringArray(R.array.choose_array);
        // Extract properties from cursor
        int type = cursor.getInt(RequirementHelper.PROJECTION_TYPE_INDEX);
        String category = cursor.getString(RequirementHelper.PROJECTION_CATEGORY_INDEX);

        // Populate fields with extracted properties
        tvHeader.setText(typenames[type]);
        tvInfo.setText(category);
    }
}
