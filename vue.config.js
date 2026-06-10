// const bootstrapSassAbstractsImports = require('vue-cli-plugin-bootstrap-vue/sassAbstractsImports.js')

const path = require('path');
const fs = require('fs');
const CopyWebpackPlugin = require('copy-webpack-plugin');

function bypassSpaNavigation(req) {
  if (!req || req.method !== 'GET') {
    return undefined;
  }

  const accept = String(req.headers && req.headers.accept ? req.headers.accept : '');
  return accept.includes('text/html') ? '/index.html' : undefined;
}

module.exports = {

  devServer: {
    host: '0.0.0.0',
    hot: false,           // Disable HMR - causing refresh loop
    liveReload: false,    // Disable live reload
    // Proxy API requests to the backend — making them same-origin from the browser's
    // perspective, so SameSite=Lax cookies (the default) flow correctly and CSRF works.
    // Admin API endpoints are listed explicitly to avoid proxying Vue Router SPA routes
    // (like /admin/category/*) which need historyApiFallback instead.
    proxy: {
      '/category': { target: 'http://localhost:8081', changeOrigin: true, bypass: bypassSpaNavigation },
      '/product': { target: 'http://localhost:8081', changeOrigin: true, bypass: bypassSpaNavigation },
      '/cart': { target: 'http://localhost:8081', changeOrigin: true, bypass: bypassSpaNavigation },
      '/wishlist': { target: 'http://localhost:8081', changeOrigin: true, bypass: bypassSpaNavigation },
      '/order': { target: 'http://localhost:8081', changeOrigin: true, bypass: bypassSpaNavigation },
      '/user': { target: 'http://localhost:8081', changeOrigin: true, bypass: bypassSpaNavigation },
      '/contact': { target: 'http://localhost:8081', changeOrigin: true, bypass: bypassSpaNavigation },
      '/login': { target: 'http://localhost:8081', changeOrigin: true, bypass: bypassSpaNavigation },
      '/logout': { target: 'http://localhost:8081', changeOrigin: true, bypass: bypassSpaNavigation },
      '/health': { target: 'http://localhost:8081', changeOrigin: true, bypass: bypassSpaNavigation },
      '/healthz': { target: 'http://localhost:8081', changeOrigin: true, bypass: bypassSpaNavigation },
      // Admin API endpoints only — NOT a catch-all for /admin/*
      // All /admin/ API calls are proxied; SPA routes (/admin, /admin/vehicles, etc.)
      // are bypassed by bypassSpaNavigation (GET with text/html accept → serves index.html).
      '/admin/': { target: 'http://localhost:8081', changeOrigin: true, bypass: bypassSpaNavigation },
    },
    historyApiFallback: true,
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
