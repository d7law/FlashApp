package com.example.wallpaperunsplash_advancedandroid.fragmentMain;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.wallpaperunsplash_advancedandroid.R;
import com.example.wallpaperunsplash_advancedandroid.RawImageActivity;
import com.example.wallpaperunsplash_advancedandroid.adapter.DownloadAdapter;
import com.example.wallpaperunsplash_advancedandroid.adapter.roomDatabase.PathDownload;
import com.example.wallpaperunsplash_advancedandroid.adapter.roomDatabase.PathRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DownloadedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DownloadedFragment extends Fragment {
    private RecyclerView rv;
    private DownloadAdapter adapter;
    private List<PathDownload> listPath;
    private PathRepository pathReponsiory;
    private String pathName;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DownloadedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpgradeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DownloadedFragment newInstance(String param1, String param2) {
        DownloadedFragment fragment = new DownloadedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        listPath = new ArrayList<>();
        pathReponsiory = new PathRepository(getActivity().getApplication());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_downloaded, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            listPath = pathReponsiory.getAllPaths();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        DownloadAdapter.OnItemClickListener listener = new DownloadAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PathDownload pathDownload) {
                Intent intent = new Intent(getContext(), RawImageActivity.class);
                intent.putExtra("pathOfDownloadPhotos", pathDownload.getPath());
                startActivity(intent);
                Toast.makeText(getContext(), "Test DownloadAdapter", Toast.LENGTH_SHORT).show();
            }
        };
        rv = view.findViewById(R.id.rvDownload);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        adapter = new DownloadAdapter(getContext(), listPath, listener);
        rv.setAdapter(adapter);
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(false);
        adapter.notifyDataSetChanged();
        listPath.size();

    }
}