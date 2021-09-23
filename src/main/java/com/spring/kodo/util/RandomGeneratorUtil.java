package com.spring.kodo.util;

public class RandomGeneratorUtil
{
    private static Character[] CHARACTERS_BASE = new Character[] {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
    };

    public static int getRandomInteger(int min, int max)
    {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static String getRandomString(int length)
    {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < length; i++)
        {
            stringBuilder.append(
                    CHARACTERS_BASE[getRandomInteger(0, CHARACTERS_BASE.length - 1)]
            );
        }

        return stringBuilder.toString();
    }
}
