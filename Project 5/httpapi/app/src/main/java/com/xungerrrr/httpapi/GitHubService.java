package com.xungerrrr.httpapi;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface GitHubService {
    @GET("/users/{user_name}/repos")
    Observable<List<Repo>> getRepo(@Path("user_name") String user_name);
    @GET("/repos/{user_name}/{repo_name}/issues")
    Observable<List<Issue>> getIssue(@Path("user_name") String user_name, @Path("repo_name") String repo_name);
    @POST("/repos/{user_name}/{repo_name}/issues")
    @Headers("Authorization: token b60eb03fa3121381a3ff78485bd6341d0f7667d7")
    Observable<Issue> postIssue(@Body Issue issue, @Path("user_name") String user_name, @Path("repo_name") String repo_name);
}
