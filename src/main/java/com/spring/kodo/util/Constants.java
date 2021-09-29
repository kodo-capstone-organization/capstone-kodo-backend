package com.spring.kodo.util;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

// Declare any program-wide constants here.
// To use in other files, import Constants and do Constants.field
public class Constants
{
    public static final String PROJECT_TITLE = "Kodo";
    public static final String PROJECT_NAME = "kodo-backend";

    public static final BigDecimal STRIPE_FEE_ACCOUNT_BASE = BigDecimal.valueOf(0.50);
    public static final BigDecimal STRIPE_FEE_PERCENTAGE = BigDecimal.valueOf(0.034);
    public static final BigDecimal PLATFORM_FEE_PERCENTAGE = BigDecimal.valueOf(0.35);

    public static final List<String> LEVELS = Arrays.asList(
            "Beginner",
            "Intermediate",
            "Expert"
    );

    public static final List<String> LANGUAGES = Arrays.asList(
            "Assembly",
            "C",
            "C#",
            "C++",
            "COBOL",
            "Common Lisp",
            "Dart",
            "F#",
            "Go",
            "Groovy",
            "Haskell",
            "Java",
            "JavaScript",
            "Kotlin",
            "MATLAB",
            "PHP",
            "Perl",
            "Python",
            "R",
            "Ruby",
            "Rust",
            "SQL",
            "Scala",
            "Swift",
            "TypeScript",
            "Visual Basic"
    );

    public static final List<String> STUDENT_NAMES = Arrays.asList(
            "Samuel", "Sunny", "Sophia", "Sofia", "Scarlett", "Savannah", "Stella", "Skylar", "Samantha", "Sarah", "Sadie", "Serenity", "Sophie", "Sydney", "Sara", "Summer", "Sloane", "Sienna", "Sawyer", "Selena", "Stephanie", "Sage", "Samara", "Shelby", "Skyler", "Scarlet", "Serena", "Skye", "Saylor", "Sabrina", "Sarai", "Sierra", "Selah", "Sylvia", "Sasha", "Skyla", "Savanna", "Shiloh", "Sutton", "Sloan", "Saige", "Siena", "Stevie", "Simone", "Sariah", "Salma", "Scarlette", "Sky", "Sariyah", "Sandra", "Selene", "Saoirse", "Susan", "Spencer", "Sonia", "Saanvi", "Samira", "Sylvie", "Scout", "Sarahi", "Saniyah", "Sharon", "Sally", "Sailor", "Shayla", "Sidney", "Samiyah", "Sherlyn", "Selina", "Shanaya", "Seraphina", "Salem", "Siya", "Sanaa", "Saniya", "Shirley", "Skylynn", "Shea", "Sapphire", "Shay", "Soraya", "Sevyn", "Shannon", "Stacy", "Sofie", "Susanna", "Skylah", "Silvia", "Saphira", "Sana", "Sahana", "Sanai", "Shreya", "Sia", "Samiya", "Sonya", "Shoshana", "Soleil", "Suri", "Sarina", "Safa"
    );

    public static final List<String> TUTOR_NAMES = Arrays.asList(
            "Trisha", "Taylor", "Trinity", "Teagan", "Tessa", "Thea", "Talia", "Tatum", "Tiffany", "Tiana", "Tatiana", "Teresa", "Tenley", "Thalia", "Tori", "Tinley", "Tinsley", "Taliyah", "Treasure", "Tegan", "Tara", "Tabitha", "Theresa", "Tamia", "Tess", "Taryn", "Taya", "Temperance", "Tania", "Tallulah", "Tia", "Tyler", "Tala", "Tina", "Tahlia", "Teigan", "Toni", "Tamara", "Tianna", "Tesla", "Teegan", "Taliah", "Taelyn", "Taraji", "Theia", "Tilly", "Taytum", "Theodora", "Taniyah", "Taylee", "Taylin", "Tanvi", "Taelynn", "Tanya", "Tayla", "Tiara", "Taleah", "Tracy", "Terra", "Talya", "Toby", "Tova", "Triniti", "Tirzah", "Tasneem", "Taylen", "Taylynn", "Tayler", "Tamar", "Tyra", "Tatyana", "Teyana", "Therese", "Timber", "Taniya", "Tammy", "Teigen", "Tatianna", "Tylee", "Tate", "Tristyn", "Tanner", "Tenzin", "Tillie", "Taleen", "Tierney", "Tyanna", "Tristan", "Taegan", "Tru", "Truly", "Tailynn", "Tahani", "Tahiry", "Tennyson", "Terri", "Teaghan", "Tyla", "Talayah", "Talitha"
    );

    public static final List<String> STUDENT_BIOGRAPHIES = Arrays.asList(
            "Hello! I am Student %s, I am interested to further my software engineering skills through this platform!",

            "My name is %s. I hope to be able to widen my knowledge on programming-related subjects!",

            "I am Student %s, nice to meet everyone! I hope to meet like-minded and passionate individuals who are in pursuit of bettering their engineering skill!"
    );

    public static final List<String> TUTOR_BIOGRAPHIES = Arrays.asList(
            "Hello! I am Tutor %s. My greatest passion in life is teaching. I was born and raised in Singapore, " +
                    "and experienced great success at school and at university due to amazing and unforgettable teachers. This is the foundation of my commitment to helping out my students, whatever their abilities may be. " +
                    "Currently, I am studying a masters degree specializing in Frontend Engineering.",

            "Welcome! I am Tutor %s. Having worked in companies such as Shopee and Tesla, I believe that my experience has been useful in curating " +
                    "courses that cater to learners of all ages and skill levels. Enroll in a course with me and let's work together to further your software engineering skills!",

            "Tutor %s here! As a previous educator in Green University and Maple Polytechnic, I believe strongly in education as a tool of enablement. " +
                    "It is my hope that the material I have uploaded onto this platform will be able to help students in understanding programming concepts that may be difficult to understand!" +
                    "Feel free to reach out to me through a session here."
    );

    public static final List<String> DISPLAY_PICTURE_URLS = Arrays.asList(
            "https://storage.googleapis.com/download/storage/v1/b/capstone-kodo-bucket/o/14103ca1-e6cb-4525-a582-7de857761e49.gif?generation=1631514505289746&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/capstone-kodo-bucket/o/1b84482f-6d29-4282-9d1d-54031a9228f6.gif?generation=1631514440607991&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/capstone-kodo-bucket/o/1f750a14-5f2a-477c-9af6-0976edb21212.gif?generation=1631514479188376&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/capstone-kodo-bucket/o/36163c2f-6702-4117-b17d-7fb15be3eb21.gif?generation=1631514450342399&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/capstone-kodo-bucket/o/38b3881e-3d31-4879-a95b-70faa1a4d236.gif?generation=1631514413799850&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/capstone-kodo-bucket/o/3a494935-bf11-4fca-a00f-c41aba4b1488.gif?generation=1631514298355330&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/capstone-kodo-bucket/o/4d8552a9-c8c9-4608-b782-3f6614168687.gif?generation=1631514402543178&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/capstone-kodo-bucket/o/539b3ea6-b8e2-4c52-8425-ecd5cfb0879b.gif?generation=1631514432269732&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/capstone-kodo-bucket/o/53b6f5d4-ce2b-4d63-8daf-1ad9ae5869c5.gif?generation=1631514459142473&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/capstone-kodo-bucket/o/95d77d1c-b161-4292-9aa2-3df33b77bda4.gif?generation=1631514520744605&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/capstone-kodo-bucket/o/d7325f84-4324-4719-a0aa-a84633687fa3.gif?generation=1631514468928145&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/capstone-kodo-bucket/o/eee736c1-90d1-4732-bd06-37cb10ad9cb9.gif?generation=1631514391025318&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/capstone-kodo-bucket/o/fecaee3c-b8af-4fa9-9145-b66030873c2b.gif?generation=1631514493573881&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/capstone-kodo-bucket/o/zdbd4b7a-4e88-45e4-9daa-3642de02a60d.gif?generation=1631514381268497&alt=media"
    );

    public static final List<String> PDF_URLS = Arrays.asList(
            ""
    );
}
