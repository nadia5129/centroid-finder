import {describe, it, expect} from "vitest";
import { buildJarArgs } from "./jarRunner.js";

describe("buildJarArgs", () => {
    it("puts the arguments in the order the JAR expects", () => {
        // Arrange
        const jarPath = "/jars/videoprocessor.jar";

        // Act 
        const args = buildJarArgs(jarPath, "/videos/intro.mp4", "/results/abc.csv", "ff0000", "120");

        // Assert
        expect(args).toEqual(["-jar", jarPath, "/videos/intro.mp4", "/results/abc.csv", "ff0000", "120"]);
    });
});