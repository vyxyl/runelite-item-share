package com.itemshare.service;

import java.io.IOException;
import java.util.function.Consumer;
import javax.inject.Inject;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ItemShareRestService
{
	@Inject
	private OkHttpClient httpClient;

	public void call(Request request, Consumer<String> onSuccess, Consumer<String> onFailure)
	{
		httpClient.newCall(request).enqueue(new Callback()
		{
			@Override
			public void onFailure(Call call, IOException exception)
			{
				onFailure.accept(exception.getMessage());
			}

			@Override
			public void onResponse(Call call, Response response)
			{
				try
				{
					String body = response.body() == null ? "" : response.body().string();

					if (response.isSuccessful())
					{
						onSuccess.accept(body);
					}
					else
					{
						onFailure.accept("Response body: " + body);
					}
				}
				catch (Exception e)
				{
					onFailure.accept(e.getMessage());
					e.printStackTrace();
				}
				finally
				{
					response.close();
				}
			}
		});
	}
}
