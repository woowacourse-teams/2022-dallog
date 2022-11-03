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
        'core-js': 3,
        proposals: true,
      },
    ],
  ],
};
