package org.vzbot.models

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import org.vzbot.plugins.geoClient
import kotlin.random.Random

/**
 *
 * @version 1.0
 * @author Devin Fritz
 */
enum class Country(val code: String, val countryName: String) {

    UNKNOWN("N/A", "N/A"),
    AFGHANISTAN("AF", "Afghanistan"),
    ALBANIA("AL", "Albania"),
    ALGERIA("DZ", "Algeria"),
    ANDORRA("AD", "Andorra"),
    ANGOLA("AO", "Angola"),
    ANTIGUA_AND_BARBUDA("AG", "Antigua and Barbuda"),
    ARGENTINA("AR", "Argentina"),
    ARMENIA("AM", "Armenia"),
    AUSTRALIA("AU", "Australia"),
    AUSTRIA("AT", "Austria"),
    AZERBAIJAN("AZ", "Azerbaijan"),
    BAHAMAS("BS", "Bahamas"),
    BAHRAIN("BH", "Bahrain"),
    BANGLADESH("BD", "Bangladesh"),
    BARBADOS("BB", "Barbados"),
    BELARUS("BY", "Belarus"),
    BELGIUM("BE", "Belgium"),
    BELIZE("BZ", "Belize"),
    BENIN("BJ", "Benin"),
    BHUTAN("BT", "Bhutan"),
    BOLIVIA("BO", "Bolivia"),
    BOSNIA_AND_HERZEGOVINA("BA", "Bosnia and Herzegovina"),
    BOTSWANA("BW", "Botswana"),
    BRAZIL("BR", "Brazil"),
    BRUNEI("BN", "Brunei"),
    BULGARIA("BG", "Bulgaria"),
    BURKINA_FASO("BF", "Burkina Faso"),
    BURUNDI("BI", "Burundi"),
    CABO_VERDE("CV", "Cabo Verde"),
    CAMBODIA("KH", "Cambodia"),
    CAMEROON("CM", "Cameroon"),
    CANADA("CA", "Canada"),
    CENTRAL_AFRICAN_REPUBLIC("CF", "Central African Republic"),
    CHAD("TD", "Chad"),
    CHILE("CL", "Chile"),
    CHINA("CN", "China"),
    COLOMBIA("CO", "Colombia"),
    COMOROS("KM", "Comoros"),
    CONGO_DEMOCRATIC_REPUBLIC_OF_THE("CD", "Democratic Republic of the Congo"),
    CONGO_REPUBLIC_OF_THE("CG", "Republic of the Congo"),
    COSTA_RICA("CR", "Costa Rica"),
    COTE_D_IVOIRE("CI", "Côte d'Ivoire"),
    CROATIA("HR", "Croatia"),
    CUBA("CU", "Cuba"),
    CYPRUS("CY", "Cyprus"),
    CZECH_REPUBLIC("CZ", "Czech Republic"),
    DENMARK("DK", "Denmark"),
    DJIBOUTI("DJ", "Djibouti"),
    DOMINICA("DM", "Dominica"),
    DOMINICAN_REPUBLIC("DO", "Dominican Republic"),
    ECUADOR("EC", "Ecuador"),
    EGYPT("EG", "Egypt"),
    EL_SALVADOR("SV", "El Salvador"),
    EQUATORIAL_GUINEA("GQ", "Equatorial Guinea"),
    ERITREA("ER", "Eritrea"),
    ESTONIA("EE", "Estonia"),
    ESWATINI("SZ", "Eswatini"),
    ETHIOPIA("ET", "Ethiopia"),
    FIJI("FJ", "Fiji"),
    FINLAND("FI", "Finland"),
    FRANCE("FR", "France"),
    GABON("GA", "Gabon"),
    GAMBIA("GM", "Gambia"),
    GEORGIA("GE", "Georgia"),
    GERMANY("DE", "Germany"),
    GHANA("GH", "Ghana"),
    GREECE("GR", "Greece"),
    GRENADA("GD", "Grenada"),
    GUATEMALA("GT", "Guatemala"),
    GUINEA_BISSAU("GW", "Guinea-Bissau"),
    GUYANA("GY", "Guyana"),
    HAITI("HT", "Haiti"),
    HONDURAS("HN", "Honduras"),
    HUNGARY("HU", "Hungary"),
    ICELAND("IS", "Iceland"),
    INDIA("IN", "India"),
    INDONESIA("ID", "Indonesia"),
    IRAN("IR", "Iran"),
    IRAQ("IQ", "Iraq"),
    IRELAND("IE", "Ireland"),
    ISRAEL("IL", "Israel"),
    ITALY("IT", "Italy"),
    JAMAICA("JM", "Jamaica"),
    JAPAN("JP", "Japan"),
    JORDAN("JO", "Jordan"),
    KAZAKHSTAN("KZ", "Kazakhstan"),
    KENYA("KE", "Kenya"),
    KIRIBATI("KI", "Kiribati"),
    KOSOVO("XK", "Kosovo"),
    KUWAIT("KW", "Kuwait"),
    KYRGYZSTAN("KG", "Kyrgyzstan"),
    LAOS("LA", "Laos"),
    LATVIA("LV", "Latvia"),
    LEBANON("LB", "Lebanon"),
    LESOTHO("LS", "Lesotho"),
    LIBERIA("LR", "Liberia"),
    LIBYA("LY", "Libya"),
    LIECHTENSTEIN("LI", "Liechtenstein"),
    LITHUANIA("LT", "Lithuania"),
    LUXEMBOURG("LU", "Luxembourg"),
    MADAGASCAR("MG", "Madagascar"),
    MALAWI("MW", "Malawi"),
    MALAYSIA("MY", "Malaysia"),
    MALDIVES("MV", "Maldives"),
    MALI("ML", "Mali"),
    MALTA("MT", "Malta"),
    MARSHALL_ISLANDS("MH", "Marshall Islands"),
    MAURITANIA("MR", "Mauritania"),
    MAURITIUS("MU", "Mauritius"),
    MEXICO("MX", "Mexico"),
    MICRONESIA("FM", "Micronesia"),
    MOLDOVA("MD", "Moldova"),
    MONACO("MC", "Monaco"),
    MONGOLIA("MN", "Mongolia"),
    MONTENEGRO("ME", "Montenegro"),
    MOROCCO("MA", "Morocco"),
    MOZAMBIQUE("MZ", "Mozambique"),
    MYANMAR("MM", "Myanmar (Burma)"),
    NAMIBIA("NA", "Namibia"),
    NAURU("NR", "Nauru"),
    NEPAL("NP", "Nepal"),
    NETHERLANDS("NL", "Netherlands"),
    NEW_ZEALAND("NZ", "New Zealand"),
    NICARAGUA("NI", "Nicaragua"),
    NIGER("NE", "Niger"),
    NIGERIA("NG", "Nigeria"),
    NORTH_KOREA("KP", "North Korea"),
    NORTH_MACEDONIA("MK", "North Macedonia"),
    NORWAY("NO", "Norway"),
    OMAN("OM", "Oman"),
    PAKISTAN("PK", "Pakistan"),
    PALAU("PW", "Palau"),
    PANAMA("PA", "Panama"),
    PAPUA_NEW_GUINEA("PG", "Papua New Guinea"),
    PARAGUAY("PY", "Paraguay"),
    PERU("PE", "Peru"),
    PHILIPPINES("PH", "Philippines"),
    POLAND("PL", "Poland"),
    PORTUGAL("PT", "Portugal"),
    QATAR("QA", "Qatar"),
    ROMANIA("RO", "Romania"),
    RUSSIA("RU", "Russia"),
    RWANDA("RW", "Rwanda"),
    SAINT_KITTS_AND_NEVIS("KN", "Saint Kitts and Nevis"),
    SAINT_LUCIA("LC", "Saint Lucia"),
    SAINT_VINCENT_AND_THE_GRENADINES("VC", "Saint Vincent and the Grenadines"),
    SAMOA("WS", "Samoa"),
    SAN_MARINO("SM", "San Marino"),
    SAO_TOME_AND_PRINCIPE("ST", "Sao Tome and Principe"),
    SAUDI_ARABIA("SA", "Saudi Arabia"),
    SENEGAL("SN", "Senegal"),
    SERBIA("RS", "Serbia"),
    SEYCHELLES("SC", "Seychelles"),
    SIERRA_LEONE("SL", "Sierra Leone"),
    SINGAPORE("SG", "Singapore"),
    SLOVAKIA("SK", "Slovakia"),
    SLOVENIA("SI", "Slovenia"),
    SOLOMON_ISLANDS("SB", "Solomon Islands"),
    SOMALIA("SO", "Somalia"),
    SOUTH_AFRICA("ZA", "South Africa"),
    SOUTH_KOREA("KR", "South Korea"),
    SOUTH_SUDAN("SS", "South Sudan"),
    SPAIN("ES", "Spain"),
    SRI_LANKA("LK", "Sri Lanka"),
    SUDAN("SD", "Sudan"),
    SURINAME("SR", "Suriname"),
    SWEDEN("SE", "Sweden"),
    SWITZERLAND("CH", "Switzerland"),
    SYRIA("SY", "Syria"),
    TAIWAN("TW", "Taiwan"),
    TAJIKISTAN("TJ", "Tajikistan"),
    TANZANIA("TZ", "Tanzania"),
    THAILAND("TH", "Thailand"),
    TIMOR_LESTE("TL", "Timor-Leste"),
    TOGO("TG", "Togo"),
    TONGA("TO", "Tonga"),
    TRINIDAD_AND_TOBAGO("TT", "Trinidad and Tobago"),
    TUNISIA("TN", "Tunisia"),
    TURKEY("TR", "Turkey"),
    TURKMENISTAN("TM", "Turkmenistan"),
    TUVALU("TV", "Tuvalu"),
    UGANDA("UG", "Uganda"),
    UKRAINE("UA", "Ukraine"),
    UNITED_ARAB_EMIRATES("AE", "United Arab Emirates"),
    UNITED_KINGDOM("GB", "United Kingdom"),
    UNITED_STATES("US", "United States"),
    URUGUAY("UY", "Uruguay"),
    UZBEKISTAN("UZ", "Uzbekistan"),
    VANUATU("VU", "Vanuatu"),
    VATICAN_CITY("VA", "Vatican City"),
    VENEZUELA("VE", "Venezuela"),
    VIETNAM("VN", "Vietnam"),
    YEMEN("YE", "Yemen"),
    ZAMBIA("ZM", "Zambia"),
    ZIMBABWE("ZW", "Zimbabwe");

    private val cachedCountries = mutableMapOf<Country, List<List<Geometry>>>()

    suspend fun getLocation(client: HttpClient): List<List<Geometry>>? {
        if (this == UNKNOWN) return null

        if (cachedCountries.containsKey(this)) {
            return cachedCountries[this]
        }


        val overpassUrl = "http://overpass-api.de/api/interpreter"
        val query = """
        [out:json];
        relation
          ["ISO3166-1"="$code"]
          [admin_level="2"]
          [type=boundary]
          [boundary=administrative];
        (._;>;);
        way(r);
        out geom;
        """.trimIndent()

        val response = client.get(overpassUrl) {
            parameter("data", query)
        }

        val geoJSON = response.body<OverpassResponse>()
        val elements = geoJSON.elements
            .filter { it.type == "way" && it.geometry != null }
            .mapNotNull { it.geometry }

        cachedCountries[this] = elements
        return elements
    }

    fun randomPointInPolygon(polygon: List<Geometry>): Pair<Double, Double>? {
        val lats = polygon.map { it.lat }
        val lons = polygon.map { it.lon }

        val minLat = lats.min()
        val maxLat = lats.max()
        val minLon = lons.min()
        val maxLon = lons.max()

        repeat(10000) { // Attempt multiple times

            val randomLat = if(minLat == maxLat) minLat else Random.nextDouble(minLat, maxLat)
            val randomLon = if(minLon == maxLon) minLon else Random.nextDouble(minLon, maxLon)

            if (pointInPolygon(randomLat, randomLon, polygon)) {
                return randomLat to randomLon
            }
        }
        return null
    }

    fun randomCoordinates(): Pair<Double, Double>? {
        return runBlocking {
            val geometry = getLocation(geoClient) ?: run {
                return@runBlocking null
            }

            var coordinates: Pair<Double, Double>? = null

            for (geometries in geometry) {
                coordinates = randomPointInPolygon(geometry.random())

                if (coordinates != null) break
            }

            return@runBlocking coordinates
        }
    }

    private fun pointInPolygon(lat: Double, lon: Double, polygon: List<Geometry>): Boolean {
        var inside = false
        val n = polygon.size
        for (i in 0 until n) {
            val xi = polygon[i].lon
            val yi = polygon[i].lat
            val xj = polygon[(i + 1) % n].lon
            val yj = polygon[(i + 1) % n].lat
            val intersect = (yi > lat) != (yj > lat) &&
                    (lon < (xj - xi) * (lat - yi) / (yj - yi) + xi)
            if (intersect) inside = !inside
        }
        return inside
    }

    companion object {
        private fun hasCountry(country: String): Boolean {
            if (country == "N/A") return false
            return entries.any { it.code.uppercase() == country.uppercase() } || entries.any { it.countryName.uppercase() == country.uppercase() }
        }

        fun getCountry(country: String): Country {
            return entries.firstOrNull { it.code.uppercase() == country.uppercase() } ?: entries.firstOrNull { it.countryName.uppercase() == country.uppercase() } ?: Country.UNKNOWN
        }

        override fun equals(other: Any?): Boolean {
            if (other == null) return false
            return hasCountry(other.toString().uppercase())
        }
    }
}

@Serializable
data class OverpassResponse(
    val elements: List<Element>
)

@Serializable
data class Element(
    val type: String,
    val id: Long,
    val geometry: List<Geometry>? = null,
    val tags: Map<String, String>? = null // For filtering by tags like `admin_level` and `boundary`
)

@Serializable
data class Geometry(
    val lat: Double,
    val lon: Double
)