package ps.g49.socialroutingclient.utils

import ps.g49.socialroutingclient.model.domainModel.Point
import java.util.ArrayList

class GoogleOverviewPolylineDecoder {

    companion object {

        fun googleOverviewPolylineDecode(encoded_polylines: String): List<Point> {
            val decodeOverviewPolyline = decodeOverviewPolyline(encoded_polylines)
            return getLocationPointsFromDecode(decodeOverviewPolyline)
        }

        private fun decodeOverviewPolyline(encoded_polylines: String): List<Double> {
            val trucks = ArrayList<Int>()
            var truck = 0
            var carriage_q = 0
            var x = 0
            val xx = encoded_polylines.length
            while (x < xx) {
                var i = encoded_polylines[x].toInt()
                i -= 63
                val _5_bits = (i shl 32 - 5).ushr(32 - 5)
                truck = truck or (_5_bits shl carriage_q)
                carriage_q += 5
                val is_last = i and (1 shl 5) == 0
                if (is_last) {
                    val is_negative = truck and 1 == 1
                    truck = truck ushr 1
                    if (is_negative) {
                        truck = truck.inv()
                    }
                    trucks.add(truck)
                    carriage_q = 0
                    truck = 0
                }
                ++x
            }
            return trucks.map {
                val doubleValue = it.toDouble()
                doubleValue.div(100000)
            }
        }

        private fun getLocationPointsFromDecode(list: List<Double>): List<Point> {
            if (list.size < 2 || list.size % 2 != 0)
                listOf<Point>()

            if (list.size == 2)
                return listOf(Point(list[0], list[1]))

            val result = ArrayList<Point>()

            var lastLat = list[0]
            var lastLong = list[1]
            result.add(Point(lastLat, lastLong))

            var idx = 2
            while (idx < list.size){
                lastLat += list[idx]
                lastLong += list[idx + 1]
                val point = Point(lastLat, lastLong)
                result.add(point)
                idx += 2
            }
            return result
        }

    }

}