const webpack = require("webpack");
const HtmlWebpackPlugin = require("html-webpack-plugin");
const MiniCssExtractPlugin = require("mini-css-extract-plugin");
const port = process.env.PORT || 3001;
var path = require("path");

module.exports = {
 context: __dirname,
 devtool: "sourcemaps",
 entry: {
  app: ["babel-polyfill", "./src/index.js"],
 },
 resolve: {
  modules: [path.join(__dirname, "src"), "node_modules"],
 },
 output: {
  path: path.resolve(__dirname, "../src/main/resources/static/"),
  publicPath: "/",
  filename: "bundle.[hash].js",
  //filename: "bundle.js",
 },
 module: {
  rules: [
   {
    test: /\.(js|jsx)$/,
    exclude: path.resolve(__dirname, "node_modules"),
    use: {
     loader: "babel-loader",
     options: {
      presets: ["@babel/preset-env", "@babel/preset-react", "@babel/flow"],
      plugins: ["@babel/plugin-proposal-class-properties"],
     },
    },
   },
   {
    test: /\.css$/,
    use: [MiniCssExtractPlugin.loader, "css-loader"],
   },
   {
    test: /\.html$/,
    use: [{ loader: "html-loader", options: { minimize: true } }],
   },
   {
    test: /\.(woff|woff2|eot|ttf|svg|png|jpg)$/,
    exclude: /node_modules/,
    loader: "file-loader",
    options: {
     limit: 1024,
     name: "[name].[ext]",
     //publicPath: "./styles/",
     //outputPath: "/styles/",
    },
   },
  ],
 },
 //devtool: "inline-source-map",
 //mode: "development",
 mode: "production",
 plugins: [
  new HtmlWebpackPlugin({
   template: "./public/index.html",
   path: path.resolve(__dirname, "../src/main/resources/static/"),
   filename: "./index.html",
   favicon: "public/favicon.ico",
   publicPath: "/",
  }),
  new MiniCssExtractPlugin({
   path: path.resolve(__dirname, "../src/main/resources/static/"),
   filename: "[name].css",
   /*filename: "[name].[hash].css",*/
   chunkFilename: "[id].css",
   publicPath: "/",
  }),
 ],
 devServer: {
  host: "localhost",
  port: port,
  historyApiFallback: true,
  open: true,
  proxy: {
   "/v1": {
    target: "http://localhost:81",
    secure: false,
    changeOrigin: true,
   },
  },
 },
};
