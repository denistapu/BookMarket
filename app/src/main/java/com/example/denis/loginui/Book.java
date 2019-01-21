package com.example.denis.loginui;

import org.json.JSONException;
import org.json.JSONObject;

public class Book {
    private int ID;
    private String titolo;
    private String casaed;
    private String autore;
    private String descrizione;
    private float prezzo;
    private String ISBN;
    private int Proprietario;


    private int quantita;
    public Book(){

    }

    public Book(int id, String titolo, String casaed, String autore, String descrizione, float prezzo, String isbn, int proprietario, int quantita) {
        ID = id;
        this.titolo = titolo;
        this.casaed = casaed;
        this.autore = autore;
        this.descrizione = descrizione;
        this.prezzo = prezzo;
        ISBN = isbn;
        Proprietario = proprietario;
        this.quantita = quantita;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getCasaed() {
        return casaed;
    }

    public void setCasaed(String casaed) {
        this.casaed = casaed;
    }

    public String getAutore() {
        return autore;
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public float getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(float prezzo) {
        this.prezzo = prezzo;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public int getProprietario() {
        return Proprietario;
    }

    public void setProprietario(int proprietario) {
        Proprietario = proprietario;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    public  void JsonToBook(JSONObject json){
        try {
            this.setAutore(json.getString("Autore"));
            this.setCasaed(json.getString("CasaEd"));
            this.setTitolo(json.getString("Titolo"));
            this.setDescrizione(json.getString("Descrizione"));
            this.setID(json.getInt("id"));
            this.setISBN(json.getString("ISBN"));
            this.setPrezzo(Float.parseFloat(json.getString("Prezzo")));
            this.setProprietario(json.getInt("Proprietario"));
            this.setQuantita(json.getInt("Quantita"));
        }catch(JSONException e){


        }
    }
}
