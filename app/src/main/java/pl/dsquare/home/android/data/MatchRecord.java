package pl.dsquare.home.android.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(tableName = "MATCHES")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchRecord {

    public static String CODE_WEB="WEBSITE_DATA";

    @PrimaryKey
    @Column(name = "ID")
    private int ID;
    @Column(name = "HOME")
    private String home;
    @Column(name = "GUEST")
    private String guest;
    @Column(name = "HOME_RESULT")
    private int homeResult;
    @Column(name = "GUEST_RESULT")
    private int guestResult;
    @Column(name = "DATE_OF_MATCH")
    private String date_of_match;
    @Column(name = "CUP")
    private String cup;
    @Column(name = "SEASON")
    private String season;
    @Column(name = "CODE_OF_DATA")
    private String code_of_data;
}
