package com.mnemstudio.WINNOW.introduction;

/**
 * Winnow (algorithm)
 * 
 * 
 * http://mnemstudio.org/neural-networks-winnow.htm
 * 
 * <pre>
 * http://en.wikipedia.org/wiki/Winnow_(algorithm)
 * </pre>
 * 
 * Introduction The Winnow algorithm is a single layer network (e.g.; perceptron).
 * 
 * Winnow was originally developed by Nick Littlestone in the late 1980's in hope of finding a faster learning algorithm
 * to compete with the slower artificial neural networks. It's called
 * "'WINNOW' because it has been designed for efficiency in separating relevant from irrelevant attributes (Littlestone, 1988)."
 * 
 * Because of Winnow's simplicity, it can be trained very quickly. Only the weights which are involved with a given
 * input are trained, while the rest are ignored until needed.
 * 
 * The most useful aspects of this algorithm are online training and the dynamic addition of input nodes. In other
 * words, the network is supposed to grow as the input becomes more diverse, and it's trained while it's in use. Normal
 * applications will often grow to include hundreds or even thousands of input nodes, so code implementations rely on
 * data structures that can expand dynamically (i.e.; static arrays won't work well).
 * 
 * In Littlestone's original design, Winnow's output was binary, so only one or two output nodes were needed. However,
 * later researchers easily added multiple output nodes.
 * 
 * The only real disadvantage to Winnow is that the number of output nodes need to be specified prior to use.
 * 
 * It's really unfortunate that WINNOW never became more popular. It found brief success in the spam filtering industry
 * between 2002 and 2005, but not much else. It's unfortunate because online training and dynamic input node addition is
 * incredibly useful.
 * 
 * 
 * @author b.bien
 *
 */
public class Introduction
{

}
