/**
 * @jest-environment jsdom
 */
import { composeStories } from '@storybook/testing-react';
import { render, screen } from '@testing-library/react';

import * as stories from './Fieldset.stories';

const { Primary, DatePicker } = composeStories(stories);

test('기본 입력 필드가 출력된다.', () => {
  render(<Primary />);

  const buttonElement = screen.getByText(/primary/i);

  expect(buttonElement).not.toBeNull();
});

test('날짜 선택을 위한 입력 필드가 출력된다.', () => {
  render(<DatePicker />);

  const buttonElement = screen.getByText(/일정 시작/i);

  expect(buttonElement).not.toBeNull();
});
