// const bootstrapSassAbstractsImports = require('vue-cli-plugin-bootstrap-vue/sassAbstractsImports.js')

//vue.config.js const path = require('path'); 
module.exports = {

  devServer: {
    hot: false,           // Disable HMR - causing refresh loop
    liveReload: false,    // Disable live reload
  },

  configureWebpack: { resolve: 
      { alias: 
        { vue: '@vue/compat' } 
      } 
    },
  // css: { loaderOptions: 
  //         { sass: 
  //           { additionalData: bootstrapSassAbstractsImports.join('\n') }, 
  //           scss: { 
  //                   additionalData: [...bootstrapSassAbstractsImports, ''].join(';\n') 
  //                 } 
  //         } 
  //     }
};
