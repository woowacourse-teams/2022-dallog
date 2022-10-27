const OPTION_HEIGHT = 36;

const PAGE_LAYOUT = {
  DEFAULT: 'default',
  SIDEBAR: 'sidebar',
};

const PALETTE = [
  '#AD1457',
  '#D81B60',
  '#D50000',
  '#E67C73',
  '#F4511E',
  '#EF6C00',
  '#F09300',
  '#F6BF26',
  '#E4C441',
  '#C0CA33',
  '#7CB342',
  '#33B679',
  '#0B8043',
  '#009688',
  '#039BE5',
  '#4285F4',
  '#3F51B5',
  '#7986CB',
  '#B39DDB',
  '#9E69AF',
  '#8E24AA',
  '#795548',
  '#616161',
  '#A79B8E',
];

const RESPONSIVE = {
  LAPTOP: {
    DEVICE: 'laptop',
    FONT_SIZE: 4,
    MIN_WIDTH: 1024,
  },
  TABLET: {
    DEVICE: 'tablet',
    FONT_SIZE: 3,
    MAX_WIDTH: 1023,
  },
  MOBILE: {
    DEVICE: 'mobile',
    FONT_SIZE: 2,
    MAX_WIDTH: 767,
  },
};

const SCHEDULE = {
  HEIGHT: 5,
  HEIGHT_WITH_MARGIN: 5.5,
};

const TRANSPARENT = 'transparent';

export { OPTION_HEIGHT, PAGE_LAYOUT, PALETTE, RESPONSIVE, SCHEDULE, TRANSPARENT };
