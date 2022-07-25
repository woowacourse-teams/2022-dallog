import { selector } from 'recoil';

import { sideBarState } from '@/recoil/atoms';

import { SELECTOR_KEY } from '@/constants';

const sideBarSelector = selector({
  key: SELECTOR_KEY.SIDE_BAR,
  get: ({ get }) => get(sideBarState),
  set: ({ set }) => set(sideBarState, (prev) => !prev),
});

export { sideBarSelector };
