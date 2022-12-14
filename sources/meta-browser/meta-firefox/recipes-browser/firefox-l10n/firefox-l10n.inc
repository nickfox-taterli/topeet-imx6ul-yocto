# Copyright (C) 2009-2019, O.S. Systems Software Ltda. All Rights Reserved
# Released under the MIT license (see packages/COPYING)

DEPENDS = "unzip-native"
RDEPENDS:${PN} = "firefox"

LICENSE = "MPL-2.0"

LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MPL-2.0;md5=815ca599c9df247a0c7f619bab123dad"

LANGUAGE = "${@get_language_name('${PN}')}"
PR = "r3"

# Mangle place where to store the file
DL_DIR:append = "/${PN}-${PV}"

SRC_URI = "https://archive.mozilla.org/pub/firefox/releases/${PV}/linux-x86_64/xpi/${LANGUAGE}.xpi"
S = "${WORKDIR}"

inherit allarch

def get_language_name(package):
    lang = package.replace('firefox-l10n-', '')
    if lang.find('-') != -1:
        lang = lang.split('-')
        lang[1] = lang[1].upper()
        lang = '-'.join(lang)

    return lang

do_configure() {
    unzip ${LANGUAGE}.xpi -d ${LANGUAGE}
}

python do_install() {
    manifest = d.getVar("LANGUAGE") + "/manifest.json"
    import json
    with open(manifest) as f:
        data = json.load(f)
    d.setVar("EXTENSION", data["applications"]["gecko"]["id"])
    bb.build.exec_func("do_install_xpi_pack", d)
}

do_install_xpi_pack() {
    install -D -m 0644 ${LANGUAGE}.xpi ${D}${libdir}/firefox/browser/extensions/${EXTENSION}.xpi
}

FILES:${PN} += "${libdir}/firefox"
