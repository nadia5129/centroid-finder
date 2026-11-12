// Builds the arguments for: java -jar <jarPath> <inputPath> <outputCsv> <targetColor> <threshold>
// "java" itself is the command, so it's not in this list.
export function buildJarArgs(
    jarPath: string, 
    inputPath: string, 
    outputCsv: string, 
    targetColor: string, 
    threshold: string
): string[] {

    return ["-jar", jarPath, inputPath, outputCsv, targetColor, threshold];
}