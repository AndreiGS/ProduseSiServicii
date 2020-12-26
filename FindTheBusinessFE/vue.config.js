module.exports = {
  pwa: {
    name: 'ProduseSiServicii.ro',
    themeColor: '#6fc42b',
    manifestCrossorigin: 'anonymous',

    workboxPluginMode: 'InjectManifest',
    workboxOptions: {
      swSrc: 'src/service-worker.js',
    },
  }
};