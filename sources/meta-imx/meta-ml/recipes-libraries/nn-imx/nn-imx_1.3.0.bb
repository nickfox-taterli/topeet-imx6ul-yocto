# Copyright 2020-2022 NXP
DESCRIPTION = "i.MX Neural Networks Accelerator Plugin"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=bba6cdb9c2b03c849ed4975ed9ed90dc"
DEPENDS = "imx-gpu-viv"

SRC_URI = "${NN_IMX_SRC};branch=${SRCBRANCH}"
NN_IMX_SRC ?= "git://github.com/nxp-imx/nn-imx.git;protocol=https"
SRCBRANCH = "imx_1.3.0"

SRCREV = "1b54f5a3e80cdf71fda9059c79d76cf7bd7e3972"

S = "${WORKDIR}/git"

EXTRA_OEMAKE += "SDKTARGETSYSROOT=${STAGING_DIR_HOST} \
                 AQROOT=`pwd` \
"
TARGET_CC_ARCH += "${LDFLAGS}"

do_install () {
    install -d ${D}${libdir}
    install -d ${D}${includedir}/OVXLIB
    install -d ${D}${includedir}/nnrt
    cp -d ${S}/*.so* ${D}${libdir}
    cp -r ${S}/include/OVXLIB/* ${D}/${includedir}/OVXLIB
    cp -r ${S}/include/nnrt/* ${D}/${includedir}/nnrt
}

# libneuralnetworks.so is dynamically loaded and thus needed in the
# main package
FILES:${PN}-dev:remove = "${libdir}/lib*.so"
FILES:${PN} += "${libdir}/libneuralnetworks${SOLIBSDEV}"
FILES:${PN}-dev += " \
    ${libdir}/libnnrt${SOLIBSDEV} \
    ${libdir}/libovxlib${SOLIBSDEV} \
"
INSANE_SKIP:${PN} += "dev-so"

# Works for i.MX 8 with GPU except for 8M Mini
COMPATIBLE_MACHINE        = "(^$)"
COMPATIBLE_MACHINE:imxgpu = "(mx8-nxp-bsp)"
COMPATIBLE_MACHINE:mx8mm-nxp-bsp  = "(^$)"
