package com.example.WhatIWear1_3;

import java.io.IOException;

/**
 * Created by Anna on 10/08/2018.
 */
public class DressNotFoundExeption extends RuntimeException
{
    public DressNotFoundExeption()
    {
        super("DressNotFoundException");
    }
}
