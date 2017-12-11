package com.example.ainge.myapplication;

public class Product
{
    private String FirstLetter;
    private String Name;


    public Product(String firstletter,String name)
    {
        this.FirstLetter = firstletter;
        this.Name = name;

    }
    public String getFirstLetter(){return FirstLetter;}
    public String getName() {return Name;}

}
