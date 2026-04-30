package inno.rmg;

public interface DataCallback<T> {
    void onSuccess(T data);
    void onError(String error);
}