package com.vaneezaahmad.sheworks

data class Job(
    var id: Int,
    val title: String,
    val company: String,
    val description: String,
    val qualifications: String,
    val specifications: String,
    val skills: String,
    val responsibilities: String,
    val salary: String,
    val benefits: String,
    val location: String,
    val jobType: String,
    val contactInfo: String,
    val logo: String,
    val timings: String,
    val createdAt: String

) {
    /*companion object {
        fun getJobs(): List<Job> {
            return listOf(
                Job(
                    "Software Engineer",
                    "Google",
                    "100k",
                    "Full Time",
                    "Mountain View, CA",
                    R.drawable.google
                ),
                Job(
                    "Product Manager",
                    "Facebook",
                    "120k",
                    "Full Time",
                    "Menlo Park, CA",
                    R.drawable.facebook
                ),
                Job(
                    "UX Designer",
                    "Apple",
                    "110k",
                    "Full Time",
                    "Cupertino, CA",
                    R.drawable.apple
                ),
                Job(
                    "Data Scientist",
                    "Amazon",
                    "130k",
                    "Full Time",
                    "Seattle, WA",
                    R.drawable.amazon
                ),
                Job(
                    "Software Engineer",
                    "Microsoft",
                    "110k",
                    "Full Time",
                    "Redmond, WA",
                    R.drawable.microsoft
                ),
                Job(
                    "Software Engineer",
                    "Netflix",
                    "120k",
                    "Full Time",
                    "Los Gatos, CA",
                    R.drawable.netflix
                ),
                Job(
                    "Software Engineer",
                    "Twitter",
                    "110k",
                    "Full Time",
                    "San Francisco, CA",
                    R.drawable.twitter
                ),
                Job(
                    "Software Engineer",
                    "Uber",
                    "120k",
                    "Full Time",
                    "San Francisco, CA",
                    R.drawable.uber
                ),
                Job(
                    "Software Engineer",
                    "Airbnb",
                    "110k",
                    "Full Time",
                    "San Francisco, CA",
                    R.drawable.airbnb
                ),
                Job(
                    "Software Engineer",
                    "Lyft",
                    "120k",
                    "Full Time",
                    "San Francisco, CA",
                    R.drawable.lyft
                )
            )
        }
    }*/
}
