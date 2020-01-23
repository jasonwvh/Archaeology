package com.archaeology.helpers

import com.archaeology.models.SiteModel

fun constructEmailTemplate(site: SiteModel): String {
    var stringifyImages = ""

    if (site.images.size > 0) {
        site.images.forEach {
            stringifyImages += "${it.uri}\n"
        }
    }

    val location =
        "https://www.google.com/maps/place//@${site.location.lat},${site.location.lng},10z"

    return "Check out this Site from Sitey!\n\n" +
            "Name: ${site.name}\nDescription: ${site.description}\n" +
            "My Rating: ${site.rating}/5 Stars\n\n" +
            "I also took some images, you can check them out here:\n $stringifyImages\n\n" +
            "You can find the site here: $location"
}
