package com.reka.tour.hotel.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class General implements Serializable {
    @SerializedName("address")
    public String address;
    @SerializedName("description")
    public String description;
    @SerializedName("latitude")
    public String latitude;
    @SerializedName("longitude")
    public String longitude;


//    "general": {
//        "address": "Dewi Sri Street, Kuta, Bali, Indonesia, Kuta, Bali",
//                "description": "%3Cb%3E%3C%2Fb%3EHotel+Sunset+Bali+terletak+di+lokasi+yang+sangat+strategis+dan+jauh+dari+kemacetan.+Hotel+di+Bali++ini+hanya+15+menit+dari+Bandara+Internasional+Ngurah+Rai+dan+10+menit+ke+Kuta+pusat+perbelanjaan.+Perjalanan+dan+pengaturan+wisata+di+Bali+dapat+dipesan+di+Receptionist.%3Cbr+%2F%3E%0ASunset+Hotel+adalah+hotel+Bintang+3+yang+menawarkan+kolam+renang+lintasan+outdoor.+Wi-Fi+tersedia+di+area+publik.+Untuk+bersantai%2C+para+tamu+dapat+menikmati+perawatan+tubuh+tradisional+Indonesia+dan+pijat+di+spa.+Dengan+meja+depan+24-jam%2C+hotel+ini+menyediakan+layanan+concierge.%3Cbr+%2F%3E%0AKamar+yang+luas+di+Hotel+Sunset+Bali+dilengkapi+dengan+dekorasi+modern%2C+pencahayaan+yang+hangat+dan+balkon+pribadi.+Setiap+seluruh+ruangan+berukuran+32m2+dan+dilengkapi+TV+layar+datar+dengan+saluran+internasional%2C+mini+bar%2C+brankas+dan+area+tempat+duduk.+Suite+kamar+mandi+memiliki+shower+air+panas.",
//                "latitude": -8.706781246367,
//                "longitude": 115.17855428728
//    }
}
