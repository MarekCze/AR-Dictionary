package ie.lit.ardictionary.ui.notebook;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ie.lit.ardictionary.R;
import ie.lit.ardictionary.adapter.NotebookAdapter;
import ie.lit.ardictionary.adapter.WordAdapter;
import ie.lit.ardictionary.model.Notebook;

public class NotebookFragment extends Fragment {
    private NotebookViewModel notebookViewModel;
    private RecyclerView notebookRecyclerView;
    private NotebookAdapter notebookAdapter;
    private List<Notebook> notebookList;
    private View root;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        notebookViewModel = new ViewModelProvider(getActivity()).get(NotebookViewModel.class);
        root = inflater.inflate(R.layout.fragment_notebook, container, false);
        notebookList = new ArrayList();
        context = getActivity();

        notebookViewModel.getUserNotebooks();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Observer<List<Notebook>> notebookObserver = new Observer<List<Notebook>>() {
            @Override
            public void onChanged(List<Notebook> notebooks) {
                Log.w("Notebook Fragment", "Inside onChanged");
                notebookRecyclerView = (RecyclerView) root.findViewById(R.id.notebook_list_recycler_view);
                notebookAdapter = new NotebookAdapter(context, notebooks);

                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(context, 1);
                notebookRecyclerView.setLayoutManager(mLayoutManager);
                notebookRecyclerView.setItemAnimator(new DefaultItemAnimator());
                notebookRecyclerView.setAdapter(notebookAdapter);
            }
        };

        notebookViewModel.getNotebookListMutableLiveData().observe(getViewLifecycleOwner(), notebookObserver);
    }
}
