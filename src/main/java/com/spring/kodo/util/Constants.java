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
            "https://storage.googleapis.com/download/storage/v1/b/capstone-kodo-bucket/o/97cd16bd-0366-49c6-b43b-f28dab927ec7.gif?generation=1632963149561382&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/capstone-kodo-bucket/o/bffabec6-cd83-497a-bf12-c3d357522b04.gif?generation=1632963160121737&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/capstone-kodo-bucket/o/673b0b74-47f9-426a-986a-17f953b89179.gif?generation=1632963166690651&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/capstone-kodo-bucket/o/53c94a6a-adcf-42f5-a542-091de7f6a5cb.gif?generation=1632963173484262&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/capstone-kodo-bucket/o/41ff343c-be4f-4631-baa0-7b7248184755.gif?generation=1632963179774696&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/capstone-kodo-bucket/o/8514c36f-1c3e-49ab-a98f-ea5ce8c77499.png?generation=1632963184850445&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/capstone-kodo-bucket/o/b36abec2-17d8-4772-b44c-9542020d570f.gif?generation=1632963192539566&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/capstone-kodo-bucket/o/1fc0a39e-95e0-4951-87b3-c3cfcd937232.gif?generation=1632963206016535&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/capstone-kodo-bucket/o/0eb92686-b072-4075-9450-9f4729f20ede.gif?generation=1632963212812240&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/capstone-kodo-bucket/o/f42409bf-4c7b-480e-92af-673ed0f97155.gif?generation=1632963220398860&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/capstone-kodo-bucket/o/9936587a-de96-431b-8d4e-88316bfd0fd7.gif?generation=1632963226219037&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/capstone-kodo-bucket/o/db557ef9-493e-49c4-9e00-9458d3ca2096.gif?generation=1632963232059315&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/capstone-kodo-bucket/o/e511a110-583b-4a4c-92c0-63675c39f0a4.gif?generation=1632963259278914&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/capstone-kodo-bucket/o/affd2909-8850-45d1-987e-ed08b186dc18.gif?generation=1632963271915915&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/capstone-kodo-bucket/o/c7823697-9a61-4811-8205-62473751bf20.gif?generation=1632963278763142&alt=media"
    );

    public static final List<String> PDF_URLS = Arrays.asList(
            "https://storage.googleapis.com/download/storage/v1/b/capstone-kodo-bucket/o/c07f7087-9136-4823-999d-b8ecb1f96ece.pdf?generation=1632963502872154&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/capstone-kodo-bucket/o/9dc018cb-29f5-4003-b33f-9bea71f32b86.pdf?generation=1632963529079476&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/capstone-kodo-bucket/o/239a4406-4e27-4984-bdfa-03d18f7c120c.pdf?generation=1632963535817471&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/capstone-kodo-bucket/o/cdccbb3a-f917-4956-9482-349bd6a51121.pdf?generation=1632963541701581&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/capstone-kodo-bucket/o/a0f58e0c-8031-46e0-988a-f361f475126c.pdf?generation=1632963547299924&alt=media"
    );

    public static final List<String> MP4_URLS = Arrays.asList(
            "https://storage.googleapis.com/download/storage/v1/b/capstone-kodo-bucket/o/e5a3537b-8bf3-4626-97cd-db48a491642d.mp4?generation=1632965650328139&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/capstone-kodo-bucket/o/e9580be0-f9d1-4b52-9151-b1bb1fa3ce8c.mp4?generation=1632965664108589&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/capstone-kodo-bucket/o/5dbf90c8-b219-443a-bf0d-a0b062f27823.mp4?generation=1632965669987164&alt=media"
    );

    public static final List<String> DOCX_URLS = Arrays.asList(
            "https://storage.googleapis.com/download/storage/v1/b/capstone-kodo-bucket/o/59f5e0e2-4352-4f32-8284-03358463acab.docx?generation=1632964583294106&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/capstone-kodo-bucket/o/5e666f82-2f69-4962-a635-081273cec007.docx?generation=1632964595859856&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/capstone-kodo-bucket/o/4818366e-cb22-4fd3-b521-ed589544cace.docx?generation=1632964602562412&alt=media"
    );

    public static final List<String> ZIP_URLS = Arrays.asList(
            "https://storage.googleapis.com/download/storage/v1/b/capstone-kodo-bucket/o/df93adca-6082-4719-b630-1fc38ac39e97.zip?generation=1632965035030081&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/capstone-kodo-bucket/o/9bdc4f05-51f4-4030-b253-c764a885caf8.zip?generation=1632965041441863&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/capstone-kodo-bucket/o/e9314925-9ae3-4fdb-a039-cb6d4ad86f82.zip?generation=1632965046662913&alt=media"
    );
}
