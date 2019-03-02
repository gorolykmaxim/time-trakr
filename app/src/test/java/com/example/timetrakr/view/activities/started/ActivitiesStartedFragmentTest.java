package com.example.timetrakr.view.activities.started;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.timetrakr.R;
import com.example.timetrakr.model.activity.events.ActivityStartEvent;
import com.example.timetrakr.view.common.recycler.EmptiableRecyclerView;
import com.example.timetrakr.view.common.recycler.ListAdapter;
import com.example.timetrakr.viewmodel.activities.started.ActivitiesStartedViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ViewModelProviders.class, Fragment.class, ItemTouchHelper.class})
public class ActivitiesStartedFragmentTest {

    private ActivitiesStartedViewModel viewModel;
    private ViewModelProvider provider;
    private Resources resources;

    @Before
    public void setUp() throws Exception {
        viewModel = Mockito.mock(ActivitiesStartedViewModel.class);
        Mockito.when(viewModel.getActivityStartEvents()).thenReturn(Mockito.mock(LiveData.class));
        Mockito.when(viewModel.getNameIsTooShortObservable()).thenReturn(Mockito.mock(LiveData.class));
        Mockito.when(viewModel.getActivityAlreadyStartedObservable()).thenReturn(Mockito.mock(LiveData.class));
        Mockito.when(viewModel.getObservableMessage()).thenReturn(Mockito.mock(LiveData.class));
        provider = Mockito.mock(ViewModelProvider.class);
        Mockito.when(provider.get(ActivitiesStartedViewModel.class)).thenReturn(viewModel);
        resources = Mockito.mock(Resources.class);
        PowerMockito.stub(PowerMockito.method(Fragment.class, "getResources")).toReturn(resources);
        PowerMockito.stub(PowerMockito.method(ItemTouchHelper.class, "attachToRecyclerView", RecyclerView.class)).toReturn(null);
    }

    @Test
    public void test() {
        Fragment fragment = new ActivitiesStartedFragment();
        PowerMockito.mockStatic(ViewModelProviders.class);
        PowerMockito.when(ViewModelProviders.of(fragment)).thenReturn(provider);
        LayoutInflater inflater = Mockito.mock(LayoutInflater.class);
        ViewGroup container = Mockito.mock(ViewGroup.class);
        ViewGroup root = Mockito.mock(ViewGroup.class);
        FloatingActionButton startActivityButton = Mockito.mock(FloatingActionButton.class);
        Mockito.when(root.findViewById(R.id.open_start_new_activity_dialog)).thenReturn(startActivityButton);
        EmptiableRecyclerView<ActivityStartEvent> recyclerView = Mockito.mock(EmptiableRecyclerView.class);
        Mockito.when(root.findViewById(R.id.activities_started_recycler_view)).thenReturn(recyclerView);
        Mockito.when(inflater.inflate(R.layout.activities_started_view, container, false)).thenReturn(root);
        View view = fragment.onCreateView(inflater, container, null);
        Assert.assertEquals(root, view);
        Mockito.verify(startActivityButton).setOnClickListener(Mockito.notNull(View.OnClickListener.class));
        Mockito.verify(recyclerView).setLayoutManager(Mockito.notNull(RecyclerView.LayoutManager.class));
        Mockito.verify(recyclerView).setListAdapter(Mockito.notNull(ListAdapter.class));
        Mockito.verify(recyclerView).setItemTouchHelper(Mockito.notNull(ItemTouchHelper.class));
        Mockito.verify(viewModel.getActivityStartEvents()).observe(Mockito.eq(fragment), Mockito.notNull(Observer.class));
        Mockito.verify(viewModel.getObservableMessage()).observe(Mockito.eq(fragment), Mockito.notNull(Observer.class));
        Mockito.verify(viewModel.getActivityAlreadyStartedObservable()).observe(Mockito.eq(fragment), Mockito.notNull(Observer.class));
        Mockito.verify(viewModel.getNameIsTooShortObservable()).observe(Mockito.eq(fragment), Mockito.notNull(Observer.class));
    }
}
