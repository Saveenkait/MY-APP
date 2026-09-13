package com.example.data.repository

import android.content.Context
import com.example.data.local.AppDatabase
import com.example.data.model.MixingPack
import com.example.data.model.RemixOrder
import com.example.data.model.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class DjMusicRepository(context: Context) {
    private val db = AppDatabase.getInstance(context)
    private val songDao = db.songDao()
    private val packDao = db.mixingPackDao()
    private val orderDao = db.remixOrderDao()

    val allSongs: Flow<List<Song>> = songDao.getAllSongs()
    val downloadedSongs: Flow<List<Song>> = songDao.getDownloadedSongs()
    val featuredSongs: Flow<List<Song>> = songDao.getFeaturedSongs()
    val allMixingPacks: Flow<List<MixingPack>> = packDao.getAllPacks()
    val allRemixOrders: Flow<List<RemixOrder>> = orderDao.getAllOrders()

    fun getSongsByGenre(genre: String): Flow<List<Song>> = songDao.getSongsByGenre(genre)
    fun searchSongs(query: String): Flow<List<Song>> = songDao.searchSongs(query)

    suspend fun insertSong(song: Song): Long = withContext(Dispatchers.IO) {
        songDao.insertSong(song)
    }

    suspend fun deleteSong(song: Song) = withContext(Dispatchers.IO) {
        songDao.deleteSong(song)
    }

    suspend fun setDownloaded(id: Long, downloaded: Boolean, path: String?) = withContext(Dispatchers.IO) {
        songDao.updateDownloadStatus(id, downloaded, path)
    }

    suspend fun incrementPlay(id: Long) = withContext(Dispatchers.IO) {
        songDao.incrementPlayCount(id)
    }

    suspend fun submitRemixOrder(order: RemixOrder): Long = withContext(Dispatchers.IO) {
        orderDao.insertOrder(order)
    }

    suspend fun addMixingPack(pack: MixingPack): Long = withContext(Dispatchers.IO) {
        packDao.insertPack(pack)
    }

    suspend fun seedInitialDataIfNeeded() = withContext(Dispatchers.IO) {
        if (songDao.getSongCount() == 0) {
            val sampleSongs = listOf(
                Song(
                    title = "Kala Chashma (Club Dance Mix)",
                    artist = "DJ Saveen",
                    genre = "Bollywood Remix",
                    bpm = 132,
                    durationSec = 224,
                    fileUrl = "https://actions.google.com/sounds/v1/science_fiction/force_field_hum_loop.ogg",
                    coverUrl = "",
                    downloadUrl = "https://actions.google.com/sounds/v1/science_fiction/force_field_hum_loop.ogg",
                    playsCount = 4820,
                    downloadsCount = 1890,
                    isFeatured = true,
                    tags = "#Bollywood #ClubMix #320kbps #HighBass",
                    description = "Official high-energy Club Dance remix. Full bass drop, clear vocal isolation, and electro dhol."
                ),
                Song(
                    title = "Dholki Tadka Punjabi Mashup",
                    artist = "DJ Saveen & DJ Beats",
                    genre = "Punjabi Dhol",
                    bpm = 126,
                    durationSec = 245,
                    fileUrl = "https://actions.google.com/sounds/v1/emergency/siren_emergency_truck.ogg",
                    coverUrl = "",
                    downloadUrl = "https://actions.google.com/sounds/v1/emergency/siren_emergency_truck.ogg",
                    playsCount = 7340,
                    downloadsCount = 3120,
                    isFeatured = true,
                    tags = "#Punjabi #Dholki #Bhangra #WeddingSpecial",
                    description = "Live heavy Punjabi dholki rhythm blended with modern synth brass and Punjabi wedding anthems."
                ),
                Song(
                    title = "Festival Electro EDM Drop 2026",
                    artist = "DJ Saveen",
                    genre = "Club EDM",
                    bpm = 130,
                    durationSec = 198,
                    fileUrl = "https://actions.google.com/sounds/v1/alarms/alarm_clock.ogg",
                    coverUrl = "",
                    downloadUrl = "https://actions.google.com/sounds/v1/alarms/alarm_clock.ogg",
                    playsCount = 3150,
                    downloadsCount = 1420,
                    isFeatured = false,
                    tags = "#EDM #Festival #Drop #FutureRave",
                    description = "Massive mainstage festival banger with progressive build-up and pounding kick-bass."
                ),
                Song(
                    title = "Choli Ke Peeche (Hardstyle Remix)",
                    artist = "DJ Saveen",
                    genre = "Bollywood Remix",
                    bpm = 138,
                    durationSec = 210,
                    fileUrl = "https://actions.google.com/sounds/v1/cartoon/cartoon_boing.ogg",
                    coverUrl = "",
                    downloadUrl = "https://actions.google.com/sounds/v1/cartoon/cartoon_boing.ogg",
                    playsCount = 9200,
                    downloadsCount = 4600,
                    isFeatured = true,
                    tags = "#Hardstyle #RetroBollywood #PartyMix",
                    description = "Modern hardstyle bassline meets classic vocals. Tested on 50,000 Watt sound systems."
                ),
                Song(
                    title = "Bhojpuri Superhit Roadshow Remix",
                    artist = "DJ Saveen Remix King",
                    genre = "Bhojpuri Mix",
                    bpm = 136,
                    durationSec = 230,
                    fileUrl = "https://actions.google.com/sounds/v1/alarms/digital_watch_alarm_long.ogg",
                    coverUrl = "",
                    downloadUrl = "https://actions.google.com/sounds/v1/alarms/digital_watch_alarm_long.ogg",
                    playsCount = 12400,
                    downloadsCount = 6800,
                    isFeatured = true,
                    tags = "#Bhojpuri #Roadshow #HighBass #Competition",
                    description = "Roadshow special sound competition mix with vibration bass and snappy dholak."
                ),
                Song(
                    title = "Tu Hai Kahan (Lo-Fi Midnight Remix)",
                    artist = "DJ Saveen Chill Studio",
                    genre = "Lo-Fi Mashup",
                    bpm = 85,
                    durationSec = 185,
                    fileUrl = "https://actions.google.com/sounds/v1/weather/rain_heavy_rain_with_thunder.ogg",
                    coverUrl = "",
                    downloadUrl = "https://actions.google.com/sounds/v1/weather/rain_heavy_rain_with_thunder.ogg",
                    playsCount = 5600,
                    downloadsCount = 2200,
                    isFeatured = false,
                    tags = "#LoFi #SlowedReverb #MidnightVibes #Acoustic",
                    description = "Slowed, reverb and vinyl crackle chill remix. Perfect for late night drive and study sessions."
                ),
                Song(
                    title = "Desi Hip-Hop Bass Booster Anthem",
                    artist = "DJ Saveen",
                    genre = "Club EDM",
                    bpm = 105,
                    durationSec = 205,
                    fileUrl = "https://actions.google.com/sounds/v1/transportation/car_horn.ogg",
                    coverUrl = "",
                    downloadUrl = "https://actions.google.com/sounds/v1/transportation/car_horn.ogg",
                    playsCount = 4100,
                    downloadsCount = 1750,
                    isFeatured = false,
                    tags = "#HipHop #SubBass #808Tuned #CarAudio",
                    description = "Tuned 808 sub-bass designed specifically for car amplifiers and woofer testing."
                )
            )
            songDao.insertAll(sampleSongs)
        }

        if (packDao.getPackCount() == 0) {
            val samplePacks = listOf(
                MixingPack(
                    title = "Bollywood Commercial Stems & FLP Pack Vol. 1",
                    category = "FLP Project Files",
                    description = "Full project files (.flp) of 5 trending club remixes with separated stems, acapellas, and master chain.",
                    includedItems = "5 Full FLP Projects, Studio Acapellas (Dry + Wet), Bassline MIDI, Drums & Claps Stems, Master Chain Presets",
                    format = "ZIP (24-Bit 44.1kHz WAV + FLP)",
                    fileSize = "850 MB",
                    priceInr = 499,
                    originalPriceInr = 1199,
                    isBestSeller = true,
                    tags = "#FLStudio #Stems #Acapellas #Commercial"
                ),
                MixingPack(
                    title = "Punjabi Heavy Dhol & Dholak Live Loops Pack",
                    category = "Dholki Loops",
                    description = "Over 180+ studio recorded authentic Indian percussions, Bhangra dhol patterns, dholki variations, and fills.",
                    includedItems = "180+ WAV Loops (120-140 BPM), One-Shots (Dagga, Tilli, Taali), Roll Fills, Algoze & Tumbi Riffs",
                    format = "WAV 24-bit Royalty Free",
                    fileSize = "620 MB",
                    priceInr = 299,
                    originalPriceInr = 699,
                    isBestSeller = true,
                    tags = "#PunjabiDhol #DholkiLoops #Percussion #Live"
                ),
                MixingPack(
                    title = "DJ Vocal Drops, Intros & Sound FX Kit 2026",
                    category = "DJ Sound FX & Drops",
                    description = "Professional male and female club DJ vocal drops, build-up risers, sirens, scratch cuts, and impact drops.",
                    includedItems = "75+ Voice Drops ('DJ in the mix', 'Are you ready', etc.), 40+ Transition Sweeps, 30+ Scratch Samples",
                    format = "WAV 320kbps MP3 + WAV",
                    fileSize = "310 MB",
                    priceInr = 199,
                    originalPriceInr = 499,
                    isBestSeller = false,
                    tags = "#VocalDrops #DJIntro #ClubFX #Scratch"
                ),
                MixingPack(
                    title = "EDM Club Banger Multitracks & Ableton Project",
                    category = "Multitrack Stems",
                    description = "Complete stems for 3 mainstage festival drops, including Serum sound presets, sidechain setup, and kick punch chains.",
                    includedItems = "3 Complete Multitrack Folders, Serum Synth Presets, Kick/Snare Layers, Automation Guide",
                    format = "ZIP (WAV + Ableton Live / FLP)",
                    fileSize = "940 MB",
                    priceInr = 599,
                    originalPriceInr = 1499,
                    isBestSeller = true,
                    tags = "#EDM #Ableton #Serum #DropStems"
                ),
                MixingPack(
                    title = "Bhojpuri Competition Vibration Bass Pack",
                    category = "Dholki Loops",
                    description = "Specialized ultra-low 35Hz-60Hz sub frequencies and punchy dholak beats engineered for roadshow speakers.",
                    includedItems = "60+ Competition Bass Loops, 40+ Sharp Dholak Patterns, Horn & Whistle One-Shots",
                    format = "WAV 24-bit High Fidelity",
                    fileSize = "420 MB",
                    priceInr = 349,
                    originalPriceInr = 799,
                    isBestSeller = false,
                    tags = "#Bhojpuri #CompetitionBass #Roadshow"
                )
            )
            packDao.insertAll(samplePacks)
        }
    }
}
