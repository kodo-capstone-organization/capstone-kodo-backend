[33mcommit 4f272e49c10a6acb5c7a4329a0799edb07505a36[m[33m ([m[1;36mHEAD -> [m[1;32mmain[m[33m)[m
Merge: a9aa531 c0c73f8
Author: Theodoric <theodorickeithlim@gmail.com>
Date:   Tue Aug 24 18:21:25 2021 +0800

    Merge branch 'main' of https://github.com/brysontzy/capstone-kodo-backend

[33mcommit a9aa5318e15075bac96eac0c9e1a34ad02611d44[m
Author: Theodoric <theodorickeithlim@gmail.com>
Date:   Tue Aug 24 18:20:03 2021 +0800

    Revert "Add QuestionOptions, QuestionOptionsRepository"
    
    This reverts commit a0318dc4c04c7b6a5cf3385413d3bcad031decb4.

[33mcommit c0c73f89e822aab8772c1aafd9bd713c55aafa33[m[33m ([m[1;31morigin/main[m[33m, [m[1;31morigin/HEAD[m[33m)[m
Author: FluffDucks <fluffducks@outlook.com>
Date:   Tue Aug 24 18:09:00 2021 +0800

    Implement upload and deleting files to GCS

[33mcommit 74eaecd7be69c7b200b6e7cf587aaf8363efedd1[m
Author: FluffDucks <fluffducks@outlook.com>
Date:   Tue Aug 24 18:08:27 2021 +0800

    Setup configs to connect to GCS for file upload

[33mcommit a0318dc4c04c7b6a5cf3385413d3bcad031decb4[m
Author: Theodoric <theodorickeithlim@gmail.com>
Date:   Tue Aug 24 15:38:38 2021 +0800

    Add QuestionOptions, QuestionOptionsRepository

[33mcommit 96ba08a379a08d28fab6abb54a5603353e4f01ce[m
Author: Theodoric <theodorickeithlim@gmail.com>
Date:   Tue Aug 24 15:24:43 2021 +0800

    Add QuestionRepository

[33mcommit 5e4c2517d55c89aebaa5d815a7f483020f9b4492[m
Author: Theodoric <theodorickeithlim@gmail.com>
Date:   Tue Aug 24 15:21:30 2021 +0800

    Add Question

[33mcommit 68d383dd7783ba0c0b70f31c90ab6cc6f4fc9893[m
Author: Theodoric <theodorickeithlim@gmail.com>
Date:   Tue Aug 24 14:58:07 2021 +0800

    Add QuizRepository

[33mcommit 51107a1d4e0475d47ef2aa3f4b3596f42293af93[m
Author: Theodoric <theodorickeithlim@gmail.com>
Date:   Tue Aug 24 14:55:42 2021 +0800

    Add MultimediaRepository

[33mcommit b29dd059434f2c694338b19701b9537ed5c64d4a[m
Author: Theodoric <theodorickeithlim@gmail.com>
Date:   Tue Aug 24 14:52:38 2021 +0800

    Add LessonRepository

[33mcommit b38caf8ae51bae6a4714167666f461c3db05b04c[m
Author: Theodoric <theodorickeithlim@gmail.com>
Date:   Tue Aug 24 14:51:46 2021 +0800

    Add EnrolledCourseRepository

[33mcommit c49138033c60d56496b9caf87655a5cf503c0899[m
Author: Theodoric <theodorickeithlim@gmail.com>
Date:   Tue Aug 24 14:50:16 2021 +0800

    Add ContentRepository

[33mcommit c890367061634770099eac77cb739616d83041e1[m
Author: Theodoric <theodorickeithlim@gmail.com>
Date:   Tue Aug 24 14:49:15 2021 +0800

    Add CompletedLessonRepository

[33mcommit f7dd0751d59e39145c0c7f9da2d0181c7b1fd3b7[m
Author: Theodoric <theodorickeithlim@gmail.com>
Date:   Tue Aug 24 14:43:05 2021 +0800

    Add Quiz

[33mcommit c982b20d36414fa8ce67890ddfd5fa3405d0f91c[m
Author: Theodoric <theodorickeithlim@gmail.com>
Date:   Tue Aug 24 14:38:25 2021 +0800

    Add Multimedia

[33mcommit 4832e53b3b834f6831909b1bedd7d7029690cbb8[m
Author: Theodoric <theodorickeithlim@gmail.com>
Date:   Tue Aug 24 14:34:55 2021 +0800

    Add QuestionType

[33mcommit 4a625feeee413f4652cc386a256fc4492699c39f[m
Author: Theodoric <theodorickeithlim@gmail.com>
Date:   Tue Aug 24 14:33:52 2021 +0800

    Add MultimediaType

[33mcommit 463e1dd80a8393da938b68f8550df9fde524a475[m
Author: Theodoric <theodorickeithlim@gmail.com>
Date:   Tue Aug 24 14:31:11 2021 +0800

    Add Content
    
    Updated getters and setters

[33mcommit cad9a14810604515bf122c93ea1b50b05235b39d[m
Author: Theodoric <theodorickeithlim@gmail.com>
Date:   Tue Aug 24 14:18:17 2021 +0800

    Add CompletedLesson
    
    Update relationships, Account - EnrolledCourse, Course - EnrolledCourse, EnrolledCourse - CompletedLesson

[33mcommit 24f09716c487c7a326ad1e12451e6cc8fe92b080[m
Author: Theodoric <theodorickeithlim@gmail.com>
Date:   Tue Aug 24 14:10:53 2021 +0800

    Add EnrolledCourse

[33mcommit 937b0a9393e0c54161d17e600198d9a29738f784[m
Author: Theodoric <theodorickeithlim@gmail.com>
Date:   Tue Aug 24 14:08:27 2021 +0800

    Add Lesson

[33mcommit f32f49b99ec5cbdecdf0138ef71935b7bec301d5[m
Author: Theodoric <theodorickeithlim@gmail.com>
Date:   Tue Aug 24 13:45:07 2021 +0800

    Add CourseService, CourseServiceImpl, CourseRepository
    
    - Cleaned Up AccountService, TagService, DatabaseConfig

[33mcommit 67f2b006d0fb8e4f1d19636300141a1a83ed61f9[m
Author: Theodoric <theodorickeithlim@gmail.com>
Date:   Tue Aug 24 13:08:43 2021 +0800

    Add Course
    
    Add Relationship for Course - Tag, Course - Account

[33mcommit 86dd51b52b24deac02f3d9db44c77ec43e3b5c45[m
Author: Theodoric <theodorickeithlim@gmail.com>
Date:   Tue Aug 24 00:37:03 2021 +0800

    Update AccountController
    
    Add login method, tested w insonmia

[33mcommit f99eeec36bceb6228b79801d07810a9a78d3238e[m
Author: Theodoric <theodorickeithlim@gmail.com>
Date:   Tue Aug 24 00:14:23 2021 +0800

    Update DatabaseConfig
    
    - Update AccountServiceImpl with login method
    - Update DatabaseConfig with "unit test"

[33mcommit e81da11c46c2f176c514a09db42ea25a1ef30a40[m
Author: Theodoric <theodorickeithlim@gmail.com>
Date:   Mon Aug 23 23:48:09 2021 +0800

    Update Account
    
    Added hashing to account's password with salt

[33mcommit fcfa151d3c69f99440baa832b5f3136bed8b43dc[m
Author: AyushK98 <65055840+AyushK98@users.noreply.github.com>
Date:   Mon Aug 23 17:46:33 2021 +0800

    Update pom.xml

[33mcommit eccd69b22355be782b5ca8baac4eefe5653b4981[m
Author: FluffDucks <fluffducks@outlook.com>
Date:   Mon Aug 23 02:18:55 2021 +0800

    Add validation to tag creation

[33mcommit e4671aa44022ed6b8f1a0c5582e88c329bfaa6dc[m
Author: FluffDucks <fluffducks@outlook.com>
Date:   Mon Aug 23 02:13:07 2021 +0800

    Implement Account creation (with taglist)

[33mcommit d50b6d56fd0a689c3376ba7db3b51ff7cf439e69[m
Author: FluffDucks <fluffducks@outlook.com>
Date:   Mon Aug 23 00:48:47 2021 +0800

    Refactored exceptions, http responses, and optional handling

[33mcommit 9ff8805395ec2fa97e43322189e84df9eedc725f[m
Author: Bryson Tan <brysontzy@hotmail.com>
Date:   Sun Aug 22 16:06:24 2021 +0800

    Added dependency to javax xml bind

[33mcommit 613b365814eab6625ac06ceb49c4b7a8dd9645db[m
Merge: 2a5eaa9 6326297
Author: Theodoric <theodorickeithlim@gmail.com>
Date:   Sun Aug 22 14:12:42 2021 +0800

    Merge branch 'main' of https://github.com/brysontzy/capstone-kodo-backend

[33mcommit 2a5eaa94516af9cb6c9489e407f410c4e10a30e5[m
Author: Theodoric <theodorickeithlim@gmail.com>
Date:   Sun Aug 22 14:10:29 2021 +0800

    Update DatabaseConfig
    
    - DatabaseConfig to use Services instead of Repositories
    - DatabaseConfig code clean up
    - Add more methods to AccountServiceImpl

[33mcommit 63262978327a13799af9130fa41b8f3e174870b8[m
Author: Bryson Tan <brysontzy@hotmail.com>
Date:   Sun Aug 22 13:37:52 2021 +0800

    Added system properties file

[33mcommit 11efa342798521c3a91172ae6d7bf37e9a55c176[m
Author: Theodoric <theodorickeithlim@gmail.com>
Date:   Sun Aug 22 12:20:11 2021 +0800

    Update Account
    
    Added Repository and Service methods

[33mcommit 7a32489877f0d688a27c016e58cb335b62a1d6ba[m
Merge: 5daa163 7526dc5
Author: Theodoric <theodorickeithlim@gmail.com>
Date:   Sun Aug 22 01:05:39 2021 +0800

    Merge branch 'main' of https://github.com/brysontzy/capstone-kodo-backend

[33mcommit 5daa16344d76ae65309f19ddf1c7da2b06597e82[m
Author: Theodoric <theodorickeithlim@gmail.com>
Date:   Sun Aug 22 01:01:56 2021 +0800

    Update DatabaseConfig
    
    Added test data with new Accounts and Tags. Tested relationship linkage with Tags added to Account

[33mcommit 2d50f5551565893e4e2996c9cab355625f789cef[m
Author: Theodoric <theodorickeithlim@gmail.com>
Date:   Sat Aug 21 23:44:29 2021 +0800

    Add Tag

[33mcommit a90c20cc35f642a38586f88b456a2cab24b6ded2[m
Author: Theodoric <theodorickeithlim@gmail.com>
Date:   Sat Aug 21 23:25:54 2021 +0800

    Update Account Entity
    
    - Add persistence and validation constraints
    - Update pom.xml w spring-boot-starter-validation 2.5.2

[33mcommit 7526dc506b4318e3d5fa0b3d56822bfef851d43f[m
Author: Bryson Tan <brysontzy@hotmail.com>
Date:   Sat Aug 21 12:32:43 2021 +0800

    Updated README for MySQL setup

[33mcommit a690b814689ff4ce6f1db31a2aa505fe0678ca7f[m
Author: FluffDucks <fluffducks@outlook.com>
Date:   Sat Aug 21 00:23:36 2021 +0800

    Implement GET methods for Account

[33mcommit bebae336acc8a1a65a9015081bed02b448a82372[m
Author: FluffDucks <fluffducks@outlook.com>
Date:   Fri Aug 20 23:31:47 2021 +0800

    Restructure project and refactor config steps

[33mcommit 2c284b49050465a3fbdd1e6181844092f8c6b8f7[m
Author: Theodoric <theodorickeithlim@gmail.com>
Date:   Fri Aug 20 18:35:09 2021 +0800

    Setup Cloud Database

[33mcommit b20eb6b96b75f7ce927bc49b21770d4d6138497b[m
Author: Theodoric <theodorickeithlim@gmail.com>
Date:   Fri Aug 20 15:27:34 2021 +0800

    Add Account package

[33mcommit 6962eb5c9ab8223f803dcc6e246ba2bf1fa77bab[m
Author: Theodoric <theodorickeithlim@gmail.com>
Date:   Fri Aug 20 15:04:56 2021 +0800

    Init SpringBoot

[33mcommit 6b607c16f85b03bf663b90d6cc2ec3f8f659b22b[m
Author: Bryson Tan <brysontzy@hotmail.com>
Date:   Tue Aug 10 22:49:31 2021 +0800

    Initial commit
