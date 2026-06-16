import { expect, test } from '@playwright/test';

test('photo detail keeps scroll position when going to next photo', async ({ page }) => {
  await page.goto('/album/100');
  await page.evaluate(() => window.scrollTo(0, document.body.scrollHeight * 0.72));
  await page.waitForTimeout(300);

  const before = await page.evaluate(() => Math.round(window.scrollY));
  await page.locator('.story-nav-button').last().click();
  await page.waitForLoadState('networkidle');
  await page.waitForTimeout(300);

  const after = await page.evaluate(() => ({
    y: Math.round(window.scrollY),
    path: window.location.pathname,
    title: document.querySelector('.photo-story-copy h1')?.textContent?.trim()
  }));

  expect(after.path).not.toBe('/album/100');
  expect(after.title).toBeTruthy();
  expect(Math.abs(after.y - before)).toBeLessThan(80);
});

test('memory detail keeps scroll position when going to next memory', async ({ page }) => {
  await page.goto('/memories/26');
  await page.evaluate(() => window.scrollTo(0, document.body.scrollHeight * 0.68));
  await page.waitForTimeout(300);

  const before = await page.evaluate(() => Math.round(window.scrollY));
  await page.locator('.memory-nav-button').last().click();
  await page.waitForLoadState('networkidle');
  await page.waitForTimeout(300);

  const after = await page.evaluate(() => ({
    y: Math.round(window.scrollY),
    path: window.location.pathname,
    title: document.querySelector('.memory-paper-heading h1')?.textContent?.trim()
  }));

  expect(after.path).not.toBe('/memories/26');
  expect(after.title).toBeTruthy();
  expect(Math.abs(after.y - before)).toBeLessThan(80);
});
