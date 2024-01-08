SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
String dataToSend = sharedPreferences.getString("yourKey", "default value");

JSONObject postData = new JSONObject();
postData.put("key", dataToSend);

OkHttpClient client = new OkHttpClient();
RequestBody body = RequestBody.create(postData.toString(), MediaType.parse("application/json; charset=utf-8"));
Request request = new Request.Builder()
    .url("https://your.api.endpoint")
    .post(body)
    .build();
client.newCall(request).enqueue(new Callback() { /* handle response */ });
