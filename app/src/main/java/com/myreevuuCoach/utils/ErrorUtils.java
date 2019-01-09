package com.myreevuuCoach.utils;

import com.myreevuuCoach.models.ErrorModelJava;
import com.myreevuuCoach.network.RetrofitClient;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

public class ErrorUtils {

    public static ErrorModelJava parseError(Response<?> response) {
        Converter<ResponseBody, ErrorModelJava> converter =RetrofitClient.retrofit
                .responseBodyConverter(ErrorModelJava.class, new Annotation[0]);

        ErrorModelJava error;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new ErrorModelJava();
        }
        return error;
    }
}
