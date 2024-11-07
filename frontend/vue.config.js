module.exports = {
  transpileDependencies: [
    'vuetify'
  ],

  devServer: {
    disableHostCheck: true
  },

  productionSourceMap: true, // 또는 'hidden-source-map'으로 설정,

  configureWebpack: {
    devtool: 'source-map' // 소스 맵 활성화
  }

}
