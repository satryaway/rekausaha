package com.jixstreet.rekatoursandtravel.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jixstreet.rekatoursandtravel.R;
import com.jixstreet.rekatoursandtravel.adapter.NewsAdapter;
import com.jixstreet.rekatoursandtravel.model.News;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


public class NewsFragment extends Fragment {
    @Bind(R.id.list_news)
    ListView listNews;

    private NewsAdapter newsAdapter;
    private News news;
    private ArrayList<News> arrayList;

    public static NewsFragment newInstance() {
        return new NewsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        ButterKnife.bind(this, view);

        arrayList = new ArrayList(30);
        for (int i = 0; i < 30; i++) {
            news = new News(
                    i + "",
                    "Lorem ipsum dolor sit amet",
                    getString(R.string.app_description));
            arrayList.add(news);
        }


        newsAdapter = new NewsAdapter(getActivity(), arrayList);
        listNews.setAdapter(newsAdapter);

        return view;
    }
}