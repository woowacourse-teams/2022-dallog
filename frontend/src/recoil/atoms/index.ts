import { atom } from 'recoil';

import { ATOM_KEY } from '@/constants';

import { getAccessToken } from '@/utils';

const sideBarState = atom({
  key: ATOM_KEY.SIDE_BAR,
  default: false,
});

const userState = atom({
  key: ATOM_KEY.USER,
  default: {
    accessToken: getAccessToken(),
  },
});

const snackBarState = atom({
  key: ATOM_KEY.SNACK_BAR,
  default: {
    text: '',
  },
});

export { snackBarState, sideBarState, userState };
