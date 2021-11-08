package com.afifemwe.bookinggedung.dummy

import com.afifemwe.bookinggedung.R
import com.afifemwe.bookinggedung.model.Fasilitas
import com.afifemwe.bookinggedung.model.Gedung
import com.afifemwe.bookinggedung.model.Time

object DummyData {

    fun generateDummyListGedung() {

        val fasilitasMusollah: Fasilitas = Fasilitas()
        fasilitasMusollah.apply {
            nama = "Musollah"
            icon = R.drawable.ic_musollah
            id = 3
        }

        val fasilitasToilet: Fasilitas = Fasilitas()
        fasilitasToilet.apply {
            nama = "Toilet"
            icon = R.drawable.ic_toilet
            id = 2
        }

        val fasilitasParkir: Fasilitas = Fasilitas()
        fasilitasParkir.apply {
            nama = "Parkir"
            icon = R.drawable.ic_parkir
            id = 1
        }

        val fasilitasAC: Fasilitas = Fasilitas()
        fasilitasAC.apply {
            nama = "AC"
            icon = R.drawable.ic_ac
            id = 4
        }

        val fasilitasMejaKursi: Fasilitas = Fasilitas()
        fasilitasMejaKursi.apply {
            nama = "Meja & Kursi"
            icon = R.drawable.ic_meja_kursi
            id = 5
        }

        val listFasilitas1 = ArrayList<Fasilitas>()
        listFasilitas1.add(fasilitasAC)
        listFasilitas1.add(fasilitasMusollah)
        listFasilitas1.add(fasilitasToilet)

        val listFasilitas2 = ArrayList<Fasilitas>()
        listFasilitas1.add(fasilitasAC)
        listFasilitas1.add(fasilitasMusollah)
        listFasilitas1.add(fasilitasToilet)
        listFasilitas1.add(fasilitasParkir)
        listFasilitas1.add(fasilitasMejaKursi)


        val time1 = Time()
        time1.apply {
            id = 1
            waktuMulai = "08:00"
            waktuSelesai = "09:00"
        }

        val time2 = Time()
        time2.apply {
            id = 2
            waktuMulai = "09:00"
            waktuSelesai = "10:00"
        }

        val time3 = Time()
        time3.apply {
            id = 3
            waktuMulai = "10:00"
            waktuSelesai = "11:00"
        }

        val listTime1 = ArrayList<Time>()
        listTime1.add(time1)
        listTime1.add(time2)

        val listTime2 = ArrayList<Time>()
        listTime2.add(time1)
        listTime2.add(time2)
        listTime2.add(time3)


        val gedung1 = Gedung()
        gedung1.apply {
            id = 1
            gambar = "https://cdn-2.tstatic.net/wartakota/foto/bank/images/gedung-juang-45-tambun-1.jpg"
            nama = "Gedung Juang 45 Bekasi"
            kapasitas = 100
            rating = 4.5
            daftarFasilitas = listFasilitas1
            jamOperasional = listTime1
            harga = 500000
            maps = "https://goo.gl/maps/2kMfVAJfcLLtPcFi7"
        }

        val gedung2 = Gedung()
        gedung2.apply {
            id = 2
            gambar = "https://www.dewiswedding.com/wp-content/uploads/2020/01/sartika.jpg"
            nama = "Gedung Sartika Bekasi"
            kapasitas = 50
            rating = 3.5
            daftarFasilitas = listFasilitas2
            jamOperasional = listTime2
            harga = 450000
            maps = "https://goo.gl/maps/yiiH7D7GjaCxqP1K9"
            pemilik = "1"
        }

    }
}