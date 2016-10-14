package com.jixstreet.rekatoursandtravel.fragment;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;
import android.widget.Toast;

import com.jixstreet.rekatoursandtravel.R;
import com.jixstreet.rekatoursandtravel.adapter.NewsAdapter;
import com.jixstreet.rekatoursandtravel.model.News;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


public class NewsFragment extends Fragment {
    @Bind(R.id.list_news)
    ListView listNews;

    @Bind(R.id.web_view)
    WebView webView;

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

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading");

        webView = (WebView) view.findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebViewClient(new WebViewClient() {
                                     public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                                         Toast.makeText(getActivity(), description, Toast.LENGTH_SHORT).show();
                                     }

                                     @Override
                                     public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                         pd.show();
                                     }


                                     @Override
                                     public void onPageFinished(WebView view, String url) {
                                         pd.dismiss();
                                     }
                                 }

        );
        webView.loadUrl("http://bit.ly/reka-news");

        return view;
    }
}