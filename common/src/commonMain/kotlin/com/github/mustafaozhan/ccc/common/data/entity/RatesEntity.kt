/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.common.data.entity

import com.github.mustafaozhan.ccc.common.model.Rates
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RatesEntity(
    @SerialName("base") var base: String = "",
    @SerialName("date") var date: String? = null,
    @SerialName("aed") var aED: Double? = null,
    @SerialName("afn") var aFN: Double? = null,
    @SerialName("all") var aLL: Double? = null,
    @SerialName("amd") var aMD: Double? = null,
    @SerialName("ang") var aNG: Double? = null,
    @SerialName("aoa") var aOA: Double? = null,
    @SerialName("ars") var aRS: Double? = null,
    @SerialName("aud") var aUD: Double? = null,
    @SerialName("awg") var aWG: Double? = null,
    @SerialName("azn") var aZN: Double? = null,
    @SerialName("bam") var bAM: Double? = null,
    @SerialName("bbd") var bBD: Double? = null,
    @SerialName("bdt") var bDT: Double? = null,
    @SerialName("bgn") var bGN: Double? = null,
    @SerialName("bhd") var bHD: Double? = null,
    @SerialName("bif") var bIF: Double? = null,
    @SerialName("bmd") var bMD: Double? = null,
    @SerialName("bnd") var bND: Double? = null,
    @SerialName("bob") var bOB: Double? = null,
    @SerialName("brl") var bRL: Double? = null,
    @SerialName("bsd") var bSD: Double? = null,
    @SerialName("btc") var bTC: Double? = null,
    @SerialName("btn") var bTN: Double? = null,
    @SerialName("bwp") var bWP: Double? = null,
    @SerialName("byn") var bYN: Double? = null,
    @SerialName("bzd") var bZD: Double? = null,
    @SerialName("cad") var cAD: Double? = null,
    @SerialName("cdf") var cDF: Double? = null,
    @SerialName("chf") var cHF: Double? = null,
    @SerialName("clf") var cLF: Double? = null,
    @SerialName("clp") var cLP: Double? = null,
    @SerialName("cnh") var cNH: Double? = null,
    @SerialName("cny") var cNY: Double? = null,
    @SerialName("cop") var cOP: Double? = null,
    @SerialName("crc") var cRC: Double? = null,
    @SerialName("cuc") var cUC: Double? = null,
    @SerialName("cup") var cUP: Double? = null,
    @SerialName("cve") var cVE: Double? = null,
    @SerialName("czk") var cZK: Double? = null,
    @SerialName("djf") var dJF: Double? = null,
    @SerialName("dkk") var dKK: Double? = null,
    @SerialName("dop") var dOP: Double? = null,
    @SerialName("dzd") var dZD: Double? = null,
    @SerialName("egp") var eGP: Double? = null,
    @SerialName("ern") var eRN: Double? = null,
    @SerialName("etb") var eTB: Double? = null,
    @SerialName("eur") var eUR: Double? = null,
    @SerialName("fjd") var fJD: Double? = null,
    @SerialName("fkp") var fKP: Double? = null,
    @SerialName("gbp") var gBP: Double? = null,
    @SerialName("gel") var gEL: Double? = null,
    @SerialName("ggp") var gGP: Double? = null,
    @SerialName("ghs") var gHS: Double? = null,
    @SerialName("gip") var gIP: Double? = null,
    @SerialName("gmd") var gMD: Double? = null,
    @SerialName("gnf") var gNF: Double? = null,
    @SerialName("gtq") var gTQ: Double? = null,
    @SerialName("gyd") var gYD: Double? = null,
    @SerialName("hkd") var hKD: Double? = null,
    @SerialName("hnl") var hNL: Double? = null,
    @SerialName("hrk") var hRK: Double? = null,
    @SerialName("htg") var hTG: Double? = null,
    @SerialName("huf") var hUF: Double? = null,
    @SerialName("idr") var iDR: Double? = null,
    @SerialName("ils") var iLS: Double? = null,
    @SerialName("imp") var iMP: Double? = null,
    @SerialName("inr") var iNR: Double? = null,
    @SerialName("iqd") var iQD: Double? = null,
    @SerialName("irr") var iRR: Double? = null,
    @SerialName("isk") var iSK: Double? = null,
    @SerialName("jep") var jEP: Double? = null,
    @SerialName("jmd") var jMD: Double? = null,
    @SerialName("jod") var jOD: Double? = null,
    @SerialName("jpy") var jPY: Double? = null,
    @SerialName("kes") var kES: Double? = null,
    @SerialName("kgs") var kGS: Double? = null,
    @SerialName("khr") var kHR: Double? = null,
    @SerialName("kmf") var kMF: Double? = null,
    @SerialName("kpw") var kPW: Double? = null,
    @SerialName("krw") var kRW: Double? = null,
    @SerialName("kwd") var kWD: Double? = null,
    @SerialName("kyd") var kYD: Double? = null,
    @SerialName("kzt") var kZT: Double? = null,
    @SerialName("lak") var lAK: Double? = null,
    @SerialName("lbp") var lBP: Double? = null,
    @SerialName("lkr") var lKR: Double? = null,
    @SerialName("lrd") var lRD: Double? = null,
    @SerialName("lsl") var lSL: Double? = null,
    @SerialName("lyd") var lYD: Double? = null,
    @SerialName("mad") var mAD: Double? = null,
    @SerialName("mdl") var mDL: Double? = null,
    @SerialName("mga") var mGA: Double? = null,
    @SerialName("mkd") var mKD: Double? = null,
    @SerialName("mmk") var mMK: Double? = null,
    @SerialName("mnt") var mNT: Double? = null,
    @SerialName("mop") var mOP: Double? = null,
    @SerialName("mro") var mRO: Double? = null,
    @SerialName("mru") var mRU: Double? = null,
    @SerialName("mur") var mUR: Double? = null,
    @SerialName("mvr") var mVR: Double? = null,
    @SerialName("mwk") var mWK: Double? = null,
    @SerialName("mxn") var mXN: Double? = null,
    @SerialName("myr") var mYR: Double? = null,
    @SerialName("mzn") var mZN: Double? = null,
    @SerialName("nad") var nAD: Double? = null,
    @SerialName("ngn") var nGN: Double? = null,
    @SerialName("nio") var nIO: Double? = null,
    @SerialName("nok") var nOK: Double? = null,
    @SerialName("npr") var nPR: Double? = null,
    @SerialName("nzd") var nZD: Double? = null,
    @SerialName("omr") var oMR: Double? = null,
    @SerialName("pab") var pAB: Double? = null,
    @SerialName("pen") var pEN: Double? = null,
    @SerialName("pgk") var pGK: Double? = null,
    @SerialName("php") var pHP: Double? = null,
    @SerialName("pkr") var pKR: Double? = null,
    @SerialName("pln") var pLN: Double? = null,
    @SerialName("pyg") var pYG: Double? = null,
    @SerialName("qar") var qAR: Double? = null,
    @SerialName("ron") var rON: Double? = null,
    @SerialName("rsd") var rSD: Double? = null,
    @SerialName("rub") var rUB: Double? = null,
    @SerialName("rwf") var rWF: Double? = null,
    @SerialName("sar") var sAR: Double? = null,
    @SerialName("sbd") var sBD: Double? = null,
    @SerialName("scr") var sCR: Double? = null,
    @SerialName("sdg") var sDG: Double? = null,
    @SerialName("sek") var sEK: Double? = null,
    @SerialName("sgd") var sGD: Double? = null,
    @SerialName("shp") var sHP: Double? = null,
    @SerialName("sll") var sLL: Double? = null,
    @SerialName("sos") var sOS: Double? = null,
    @SerialName("srd") var sRD: Double? = null,
    @SerialName("ssp") var sSP: Double? = null,
    @SerialName("std") var sTD: Double? = null,
    @SerialName("stn") var sTN: Double? = null,
    @SerialName("svc") var sVC: Double? = null,
    @SerialName("syp") var sYP: Double? = null,
    @SerialName("szl") var sZL: Double? = null,
    @SerialName("thb") var tHB: Double? = null,
    @SerialName("tjs") var tJS: Double? = null,
    @SerialName("tmt") var tMT: Double? = null,
    @SerialName("tnd") var tND: Double? = null,
    @SerialName("top") var tOP: Double? = null,
    @SerialName("try") var tRY: Double? = null,
    @SerialName("ttd") var tTD: Double? = null,
    @SerialName("twd") var tWD: Double? = null,
    @SerialName("tzs") var tZS: Double? = null,
    @SerialName("uah") var uAH: Double? = null,
    @SerialName("ugx") var uGX: Double? = null,
    @SerialName("usd") var uSD: Double? = null,
    @SerialName("uyu") var uYU: Double? = null,
    @SerialName("uzs") var uZS: Double? = null,
    @SerialName("ves") var vES: Double? = null,
    @SerialName("vnd") var vND: Double? = null,
    @SerialName("vuv") var vUV: Double? = null,
    @SerialName("wst") var wST: Double? = null,
    @SerialName("xaf") var xAF: Double? = null,
    @SerialName("xag") var xAG: Double? = null,
    @SerialName("xau") var xAU: Double? = null,
    @SerialName("xcd") var xCD: Double? = null,
    @SerialName("xdr") var xDR: Double? = null,
    @SerialName("xof") var xOF: Double? = null,
    @SerialName("xpd") var xPD: Double? = null,
    @SerialName("xpf") var xPF: Double? = null,
    @SerialName("xpt") var xPT: Double? = null,
    @SerialName("yer") var yER: Double? = null,
    @SerialName("zar") var zAR: Double? = null,
    @SerialName("zmw") var zMW: Double? = null,
    @SerialName("zwl") var zWL: Double? = null
)

fun RatesEntity.toModel() = Rates(
    base, date, aED, aFN, aLL, aMD, aNG, aOA, aRS, aUD, aWG, aZN, bAM, bBD, bDT, bGN, bHD, bIF,
    bMD, bND, bOB, bRL, bSD, bTC, bTN, bWP, bYN, bZD, cAD, cDF, cHF, cLF, cLP, cNH, cNY, cOP,
    cRC, cUC, cUP, cVE, cZK, dJF, dKK, dOP, dZD, eGP, eRN, eTB, eUR, fJD, fKP, gBP, gEL, gGP,
    gHS, gIP, gMD, gNF, gTQ, gYD, hKD, hNL, hRK, hTG, hUF, iDR, iLS, iMP, iNR, iQD, iRR, iSK,
    jEP, jMD, jOD, jPY, kES, kGS, kHR, kMF, kPW, kRW, kWD, kYD, kZT, lAK, lBP, lKR, lRD, lSL,
    lYD, mAD, mDL, mGA, mKD, mMK, mNT, mOP, mRO, mRU, mUR, mVR, mWK, mXN, mYR, mZN, nAD, nGN,
    nIO, nOK, nPR, nZD, oMR, pAB, pEN, pGK, pHP, pKR, pLN, pYG, qAR, rON, rSD, rUB, rWF, sAR,
    sBD, sCR, sDG, sEK, sGD, sHP, sLL, sOS, sRD, sSP, sTD, sTN, sVC, sYP, sZL, tHB, tJS, tMT,
    tND, tOP, tRY, tTD, tWD, tZS, uAH, uGX, uSD, uYU, uZS, vES, vND, vUV, wST, xAF, xAG, xAU,
    xCD, xDR, xOF, xPD, xPF, xPT, yER, zAR, zMW, zWL
)
