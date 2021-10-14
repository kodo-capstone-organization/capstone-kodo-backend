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
                    "It is my hope that the material I have uploaded onto this platform will be able to help students in understanding programming concepts that may be difficult to understand! " +
                    "Feel free to reach out to me through a session here."
    );

    public static final List<String> DISPLAY_PICTURE_URLS = Arrays.asList(
            "https://storage.googleapis.com/download/storage/v1/b/kodo-capstone-bucket/o/9ca368fe-c889-4017-bcfd-10bd7e4106c7.gif?generation=1634011614151231&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/kodo-capstone-bucket/o/48058e88-adb5-4c27-b663-9a99ec7f7e32.gif?generation=1634011669482862&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/kodo-capstone-bucket/o/a50f64d5-416e-4da6-ba80-9344b8241940.gif?generation=1634011675344910&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/kodo-capstone-bucket/o/71cf29b7-da0b-4699-ae2d-853bd09a779c.gif?generation=1634011681859491&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/kodo-capstone-bucket/o/453de803-272d-449c-a4a4-eaf7d3822219.gif?generation=1634011689225405&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/kodo-capstone-bucket/o/b1d7244b-0d0e-424f-b8ec-c7dabe960518.gif?generation=1634011696323864&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/kodo-capstone-bucket/o/01635a66-664a-43cc-9415-d0df520e4ba1.gif?generation=1634011701601496&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/kodo-capstone-bucket/o/d11e1e01-ad55-4026-8e92-ccc4db1d7abe.gif?generation=1634011707447331&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/kodo-capstone-bucket/o/67b12319-413a-4edc-84a8-7a9ba3c4e2de.gif?generation=1634011722605701&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/kodo-capstone-bucket/o/9866564d-d329-42b2-bc6c-1da2d713be18.gif?generation=1634011729029255&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/kodo-capstone-bucket/o/55bceaf4-4739-43dc-9239-552d25379c11.gif?generation=1634011734096369&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/kodo-capstone-bucket/o/68d99680-2e66-4952-82b1-ace6264f2dec.gif?generation=1634011739698120&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/kodo-capstone-bucket/o/1385fe45-29f1-4dc0-b453-c84ae852016f.gif?generation=1634011746103612&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/kodo-capstone-bucket/o/ba28c703-d399-433c-8458-ef82b02bc018.gif?generation=1634011753132018&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/kodo-capstone-bucket/o/532fae92-ade2-458d-a63b-8bf57ad1d0c1.gif?generation=1634200294027079&alt=media"
    );

    public static final List<String> PDF_URLS = Arrays.asList(
            "https://storage.googleapis.com/download/storage/v1/b/kodo-capstone-bucket/o/5eed2d70-befc-4168-a064-41c46acde76d.pdf?generation=1634011939916828&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/kodo-capstone-bucket/o/32c7c111-aa1f-467d-a45c-626715a204f5.pdf?generation=1634011946445529&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/kodo-capstone-bucket/o/34f18173-1677-4f2b-9e91-15b9503c5ac6.pdf?generation=1634011951238992&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/kodo-capstone-bucket/o/2401997d-d386-4e50-a419-870d374ec706.pdf?generation=1634011956622391&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/kodo-capstone-bucket/o/20d26fd5-7b72-4640-88ad-562349012232.pdf?generation=1634011961283717&alt=media"
    );

    public static final List<String> MP4_URLS = Arrays.asList(
            "https://storage.googleapis.com/download/storage/v1/b/kodo-capstone-bucket/o/b4f7d56b-f101-4125-93c9-4229f35898e8.mp4?generation=1634012220588838&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/kodo-capstone-bucket/o/40b8e6dd-8a48-439f-98e0-ad9ce628d62a.mp4?generation=1634012226230477&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/kodo-capstone-bucket/o/a9d7e058-9b76-4b5a-b479-c1baa06366f9.mp4?generation=1634012230938287&alt=media"
    );

    public static final List<String> DOCX_URLS = Arrays.asList(
            "https://storage.googleapis.com/download/storage/v1/b/kodo-capstone-bucket/o/e0359cc2-8ce7-4385-a448-4310156e0287.docx?generation=1634012041177623&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/kodo-capstone-bucket/o/1f5daa97-27e0-43e3-8600-e96e7716d1a5.docx?generation=1634012047026187&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/kodo-capstone-bucket/o/d1b1bef4-2811-4d3d-9ced-92a22867aa88.docx?generation=1634012053344599&alt=media"
    );

    public static final List<String> ZIP_URLS = Arrays.asList(
            "https://storage.googleapis.com/download/storage/v1/b/kodo-capstone-bucket/o/b9de9595-070c-48e1-b91f-2d59599c80d7.zip?generation=1634012123563947&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/kodo-capstone-bucket/o/16d3c971-a41b-4a57-bfc9-f23f9ea3e5fc.zip?generation=1634012129111475&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/kodo-capstone-bucket/o/f3e2fcb7-f22a-4b57-8514-20587f0bc177.zip?generation=1634012134144687&alt=media"
    );

    public static final List<String> PNG_URLS = Arrays.asList(
            "https://storage.googleapis.com/download/storage/v1/b/kodo-capstone-bucket/o/604b43b8-4652-4bea-a36f-2d616b31547c.png?generation=1634012358353872&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/kodo-capstone-bucket/o/02a01782-6eee-49dd-a488-da411c5ab252.png?generation=1634012362273728&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/kodo-capstone-bucket/o/5bb3451c-18ed-4d7f-b271-2076e4aba7bd.png?generation=1634012366453941&alt=media"
    );

    public static final List<String> BANNER_URLS = Arrays.asList(
            "https://storage.googleapis.com/download/storage/v1/b/kodo-capstone-bucket/o/44897ebe-71d7-46e6-9a95-c7b3c290372a.png?generation=1634012281223330&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/kodo-capstone-bucket/o/af9702a6-a6a5-4dfe-83c9-cdea00e6104d.png?generation=1634012286602439&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/kodo-capstone-bucket/o/a972ef90-cfe1-458b-bfc9-aa15e59143cf.png?generation=1634012291807567&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/kodo-capstone-bucket/o/c3285de7-e96b-403e-9ec9-f9cb1e14323b.png?generation=1634012296090137&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/kodo-capstone-bucket/o/05058c8d-014e-42e6-9e88-e71fdad41f77.png?generation=1634012300260618&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/kodo-capstone-bucket/o/4ae42446-21c8-47c2-84b6-b98ac822279f.png?generation=1634012304485156&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/kodo-capstone-bucket/o/069b3d6d-b8f4-458b-9544-046eedb4380b.png?generation=1634012308892399&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/kodo-capstone-bucket/o/3d750230-4847-4efb-b84e-53bb70fd780a.png?generation=1634012314247761&alt=media",
            "https://storage.googleapis.com/download/storage/v1/b/kodo-capstone-bucket/o/dfbf18bf-4982-4b2b-8a6d-bb80470d7d5b.png?generation=1634012318353923&alt=media"
    );
}
