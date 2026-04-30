package inno.rmg;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import inno.rmg.databinding.FragmentHomeBinding;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private GameCardAdapter adapter;

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new GameCardAdapter(requireContext(), DataManager.getInstance().getGames());
        binding.rvGames.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvGames.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.updateData(DataManager.getInstance().getGames());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}