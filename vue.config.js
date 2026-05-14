// const bootstrapSassAbstractsImports = require('vue-cli-plugin-bootstrap-vue/sassAbstractsImports.js')

const path = require('path');
const fs = require('fs');
const CopyWebpackPlugin = require('copy-webpack-plugin');

module.exports = {

  devServer: {
    hot: false,           // Disable HMR - causing refresh loop
    liveReload: false,    // Disable live reload
    setupMiddlewares: (middlewares, devServer) => {
      if (!devServer || !devServer.app) {
        return middlewares;
      }

      const appImagesRoot = path.resolve(__dirname, 'src/assets/AppImages');

      // Reliable static files for /AppImages/* (handles %20, etc.)
      devServer.app.use('/AppImages', (req, res, next) => {
        try {
          const raw = (req.originalUrl || req.url || '').split('?')[0];
          const stripped = raw.replace(/^\/AppImages\/?/, '');
          const relative = decodeURIComponent(stripped).replace(/^\/+/, '');
          const filePath = path.resolve(appImagesRoot, relative);
          if (!filePath.startsWith(appImagesRoot)) {
            return next();
          }
          if (!fs.existsSync(filePath) || !fs.statSync(filePath).isFile()) {
            return next();
          }
          res.sendFile(filePath);
        } catch (e) {
          next();
        }
      });

      devServer.app.get('/health', (_, res) => {
        res.status(200).json({
          status: "healthy",
          service: "frontend",
          message: "Service is healthy",
        });
      });

      return middlewares;
    },
  },

  configureWebpack: {
    resolve: {
      alias: {
        vue: '@vue/compat',
      },
    },
    plugins: [
      new CopyWebpackPlugin({
        patterns: [
          {
            from: path.resolve(__dirname, 'src/assets/AppImages'),
            to: 'AppImages',
            noErrorOnMissing: true,
          },
        ],
      }),
    ],
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
