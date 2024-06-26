package gr.aueb;

import com.google.gson.annotations.SerializedName;

public class Crew {
    @SerializedName("name")
    private String name;
    @SerializedName("job")
    private String job;
    
    public String getName() {
        return name;
    }

    public String getJob() {
        return job;
    }

    @Override
    public String toString() {
        return String.format("Name: %-30s\tJob: %s", name, job + "\n");
    }
}
