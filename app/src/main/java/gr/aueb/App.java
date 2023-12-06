package gr.aueb;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Scanner;

public class App 
{
    public static void main( String[] args ) throws Exception
    {
        
        Scanner sc = new Scanner(System.in);
        Gson gson = new Gson();
        File f1 = new File("C:\\Users\\Nick\\api_keys\\tmdb_api_key.txt");
        String apiKey = null;

        try (BufferedReader br = new BufferedReader(new FileReader(f1))) {
            apiKey = br.readLine();
        } catch (Exception e) {
            // TODO: handle exception
        }


        System.out.println("Search for a movie. \n");
        String searchInput = sc.nextLine();        
        ArrayList<Integer> ids = Movie.movieSearch(searchInput, apiKey);
        System.out.println("\nChoose your title. \n");
        int answer = sc.nextInt();
        boolean exists = Movie.localSearch(ids.get(answer - 1));
        
        if(exists) {
            //print details from project's data base 
        } else {
            //responce for movie credits
             HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/movie/" + ids.get(answer - 1) + "/credits?language=en-US"))
                .header("accept", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
            HttpResponse<String> response1 = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());
        
            //responce for movie details
            request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/movie/" + ids.get(answer - 1) + "?language=en-US"))
                .header("accept", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
            HttpResponse<String> response2 = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        
            Contributors creditsResponse = gson.fromJson(response1.body(), Contributors.class);
            MovieDetails movieDetailsResponse = gson.fromJson(response2.body(), MovieDetails.class);

            //System.out.println(creditsResponse);
            //System.out.println(movieDetailsResponse);

            Movie movie = new Movie(creditsResponse, movieDetailsResponse);
            System.out.println(movie);
            //add in project's data base
        }
        
        sc.close();

    }
}