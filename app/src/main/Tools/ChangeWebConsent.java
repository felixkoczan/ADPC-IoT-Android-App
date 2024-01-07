HttpClient client = HttpClient.newHttpClient();

HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:8081/"))
        .header("Content-Type", "application/json")  // Setting the Content-Type header
        .header("Authorization", "Bearer your-token-here")  // Setting an Authorization header
        .header("Custom-Header", "CustomValue")  // Setting a custom header
        .POST(HttpRequest.BodyPublishers.ofString("Your message here"))
        .build();

client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
        .thenApply(HttpResponse::body)
        .thenAccept(System.out::println)
        .join();
