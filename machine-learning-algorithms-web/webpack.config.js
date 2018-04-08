'use strict';

const path = require('path');
const webpack = require('webpack');
const merge = require('webpack-merge');
const DefinePlugin = require('webpack/lib/DefinePlugin');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const CopyWebpackPlugin = require('copy-webpack-plugin');
const ExtractTextPlugin = require('extract-text-webpack-plugin');

const nodeEnv = process.env.NODE_ENV || 'development';

const commonConfig = {

    resolve: {
        modules: [path.resolve('./src'), 'node_modules'],
        extensions: ['.js', '.csv', '.json', '.less', '.css', '.scss', '.html']
    },

    module: {
        rules: [
            {
                test:/\.js$/,
                exclude: /node_modules/,
                enforce: 'pre',
                use: [{loader: 'eslint-loader', options: {configFile: '.eslintrc'}}]
            },
            {
                use: 'babel-loader',
                test: /\.js$/,
                exclude: /node_modules/
            },
            {
                test: /\.html$/,
                use: [{loader: 'htmlhint-loader', options: {configFile: '.htmlhintrc'}}],
                exclude: /node_modules/,
                enforce: 'pre'
            },
            {
                test: /\.(png|jpg|jpeg|svg|gif|woff|woff2|ttf|eot)(\?v=\d+\.\d+\.\d+)?$/,
                use: 'file-loader'
            },
            {
                use: [{loader: 'html-loader'}],
                test: /\.html$/
            }
        ]
    },
    plugins: [
        new DefinePlugin({
            'process.env': {
                NODE_ENV: JSON.stringify(nodeEnv)
            }
        }),
        new webpack.optimize.CommonsChunkPlugin({
            names: ['vendor', 'manifest']
        }),
        new CopyWebpackPlugin([{
            from: __dirname + '/src/images',
            to: 'images'
        }]),
        new HtmlWebpackPlugin({
            template: 'src/index.html',
            chunksSortMode: 'dependency'
        })
    ]
};

const devConfig = {
    entry: {
        main: ['react-hot-loader/patch', 'webpack-dev-server/client', 'webpack/hot/only-dev-server', 'main.js']
    },

    devtool: 'inline-source-map',

    output: {
        path: path.join(__dirname, '/build'),
        filename: '[name].bundle.js',
        publicPath: '/'
    },

    module: {
        rules: [
            {
                test: /\.less$/,
                include: path.resolve(__dirname, 'src/styles'),
                use: ['style-loader', 'css-loader', 'less-loader']
            },
            {
                test: /\.css$/,
                use: ['style-loader', 'css-loader']
            },
            {
                test: /\.(sass|scss)$/,
                use: ['css-loader', 'sass-loader']
            }
        ]
    },

    devServer: {
        contentBase: 'src',
        compress: true,
        hot: true,
        port: 9000,
        host: '0.0.0.0',
        disableHostCheck: true,
        historyApiFallback: {
            disableDotRule: true
        },
        stats: 'minimal',
        overlay: true,
        proxy: {
            '/api': 'http://localhost:8080'
        }
    },

    plugins: [
        new webpack.HotModuleReplacementPlugin(),
        new webpack.NamedModulesPlugin()
    ]
};

const prodConfig = {
    entry: {
        main: ['main.js']
    },

    devtool: 'source-map',

    output: {
        path: path.join(__dirname, '/build'),
        filename: '[name].[hash].bundle.js',
        publicPath: '/'
    },

    module: {
        rules: [
            {
                test: /\.less$/,
                include: path.resolve(__dirname, 'src/styles'),
                use: ExtractTextPlugin.extract({
                    fallback: 'style-loader',
                    use: ['css-loader', 'less-loader']
                })
            },
            {
                test: /\.css$/,
                use: ExtractTextPlugin.extract({
                    fallback: 'style-loader',
                    use: 'css-loader'
                })
            },
            {
                test: /\.(sass|scss)$/,
                use: ExtractTextPlugin.extract({
                    fallback: 'style-loader',
                    use: ['css-loader', 'sass-loader']
                })
            }
        ]
    },

    plugins: [
        new ExtractTextPlugin({filename: '[name].[hash].css'}),
        new webpack.NoEmitOnErrorsPlugin()
    ]
};

let config;
switch (nodeEnv) {
    case 'production':
        console.info('NODE_ENV: production');
        config = merge(commonConfig, prodConfig);
        break;
    default:
        console.info('NODE_ENV: development');
        config = merge(commonConfig, devConfig);
        break;
}

module.exports = config;
