package com.example.linkedin_clone.DataClasses

class User {
    private var name: String = ""
    private var email: String = ""
    private var pronouns: String = ""
    private var headline: String = ""
    private var industry: String = ""
    private var education: String = ""
    private var uid: String = ""
    private var profileImageURL: String = ""
    private var coverImageURL: String = ""

    constructor()
    constructor(
        name: String,
        email: String,
        pronouns: String,
        headline: String,
        industry: String,
        education: String,
        uid: String,
        profileImageURL: String,
        coverImageURL: String,
    ) {
        this.name = name
        this.email = email
        this.pronouns = pronouns
        this.headline = headline
        this.industry = industry
        this.education = education
        this.uid = uid
        this.profileImageURL = profileImageURL
        this.coverImageURL = coverImageURL
    }


    fun getName(): String {
        return name
    }

    fun setName(mobile: String) {
        this.name = mobile
    }

    fun getEmail(): String {
        return email
    }

    fun setEmail(mobile: String) {
        this.email = mobile
    }
    fun getUID() : String {
        return uid
    }
    fun setUID(uid: String) {
        this.uid = uid
    }
    fun getPronoun() : String {
        return pronouns
    }
    fun setPronoun(pronouns: String) {
        this.pronouns= pronouns
    }
    fun getHeadline() : String {
        return headline
    }
    fun setHeadline(headline: String) {
        this.headline = headline
    }
    fun getIndustry() : String {
        return industry
    }
    fun setIndustry(industry: String) {
        this.industry = industry
    }
    fun getEducation() : String {
        return education
    }
    fun setEducation(education: String) {
        this.education = education
    }
    fun getProfileImageURL() : String {
        return profileImageURL
    }
    fun setProfileImageURL(profileImageURL: String) {
        this.profileImageURL = profileImageURL
    }
    fun getCoverImageURL() : String {
        return coverImageURL
    }
    fun setCoverImageURL(coverImageURL: String) {
        this.coverImageURL = coverImageURL
    }
}