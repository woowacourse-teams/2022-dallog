module.exports = {
  presets: [
    '@babel/preset-react',
    '@babel/preset-env',
    '@babel/preset-typescript',
    '@emotion/babel-preset-css-prop',
  ],
  plugins: [
    '@emotion',
    [
      '@babel/plugin-transform-runtime',
      {
        corejs: 3,
        proposals: true,
      },
    ],
  ],
};
