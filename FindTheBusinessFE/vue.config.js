module.exports = {
  pwa: {
    name: "ProduseSiServicii.ro",
    themeColor: "#6fc42b",
    manifestCrossorigin: "anonymous",
    appleMobileWebAppCapable: "yes",
    appleMobileWebAppStatusBarStyle: "black",

    workboxPluginMode: "InjectManifest",
    workboxOptions: {
      swSrc: "src/service-worker.js",
    },
    manifestOptions: {
      name: "ProduseSiServicii.ro",
      short_name: "P&S.ro",
      start_url: ".",
      display: "standalone",
      theme_color: "#6fc42b",
    }
  }
};